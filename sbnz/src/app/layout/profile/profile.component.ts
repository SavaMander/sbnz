import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AddressChangeRequest } from 'src/app/dto/AddressChangeRequest';
import { GetOrdersRequest } from 'src/app/dto/GetOrdersRequest';
import { MessageResponse } from 'src/app/dto/MessageResponse';
import { Profile } from 'src/app/dto/Profile';
import { AuthService } from 'src/app/services/auth.service';
import { OrderService } from 'src/app/services/order.service';

interface UserInfo {
  username: string;
  email: string;
  address: string;
  phoneNumber: string;
}

interface OrderItem {
  name: string;
  quantity: number;
}

interface Order {
  id: string;
  restaurantName: string;
  restaurantImage: string;
  orderDate: string;
  deliveryDate?: string;

  items: OrderItem[];
  totalAmount: number;
  deliveryAddress: string;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  constructor(private authService: AuthService, private router: Router, private orderService: OrderService) {
  }
Logout() {
localStorage.removeItem('user');
    this.authService.setUser(); // Notify AuthService that user state changed
    this.router.navigate(['/home']);
}
  userInfo: UserInfo = {
    username: '',
    email: '',
    phoneNumber: '',
    address: '',
  };
  orders: Order[] = [];
  filteredOrders: Order[] = [];
  showPromoCodeDialog: boolean = false;
  promoCodeForm: { code: string } = { code: '' };
  role: string = '';
  showEditProfile: boolean = false;
  editForm = {
    address: '',
    city: ''
  };

  private mapOrderListToItems(orderListString: string): OrderItem[] {
    return orderListString.split(',').map(item => {
      const parts = item.trim().split(' x ');
      const quantity = parseInt(parts[0], 10) || 1;
      const name = parts.length > 1 ? parts[1].trim() : parts[0].trim();

      return {
        name: name,
        quantity: quantity
      };
    });
  }

  selectedStatusFilter: string = 'all';

  ngOnInit(): void {
    // 1. Pozivanje servisa za dohvat podataka o profilu
    this.role = this.authService.getRole();
    this.authService.profile(this.authService.getUsername()).subscribe({
      next: (profileData: Profile) => {
        // 2. Mapiranje primljenog DTO-a na UserInfo za prikaz
        this.userInfo.username = profileData.username;
        this.userInfo.email = profileData.email;
        this.userInfo.address = profileData.address;
        this.userInfo.phoneNumber = profileData.phoneNumber;
        this.editForm.address = this.userInfo.address;
        
        this.loadOrders(this.authService.getUsername());
        console.log('Profil je uspešno učitan:', profileData);
      },
      error: (err) => {
        console.error('Greška pri učitavanju profila:', err);
        this.Logout(); // Može preusmeriti korisnika na login
      }
    });
  }

  private mapToFrontendOrder(dto: GetOrdersRequest): Order {
    return {
      id: dto.orderId,
      restaurantName: dto.restaurant,
      // Status i slika restorana su hardkodovani/defaultni jer nedostaju u DTO-u
      restaurantImage: '🍕', 
      orderDate: dto.creationDate,
      deliveryDate: undefined, 
      items: this.mapOrderListToItems(dto.orderList),
      totalAmount: dto.totalPrice,
      // Adresa isporuke je hardkodovana dok je ne dobijemo sa beackenda
      deliveryAddress: this.userInfo.address || 'N/A' 
    };
  }

  loadOrders(email: string): void {
    this.orderService.getMyOrders(email).subscribe({
      next: (ordersDto: GetOrdersRequest[]) => {
        // Mapiranje backend liste na frontend listu
        this.orders = ordersDto.map(dto => this.mapToFrontendOrder(dto));
        console.log('Porudžbine uspešno učitane:', this.orders);
      },
      error: (err) => {
        console.error('Greška pri učitavanju porudžbina:', err);
        alert('Nije moguće učitati porudžbine.');
      }
    });
  }

  openEditProfile(): void {
    this.showEditProfile = true;
    this.editForm = {
      address: '',
      city: ''
    };
  }

  cancelOrder(order: Order): void {
    if (!confirm(`Are you sure you want to cancel order for ${order.restaurantName}, Price: ${order.totalAmount} (ID: ${order.id})?`)) {
      return;
    }
    
    const email = this.authService.getUsername();

    this.orderService.cancelOrder(order.id, email).subscribe({
      next: (response: MessageResponse) => {
        if (response.successful) {
          alert('Order successfully cancelled!');
          window.location.reload();
        } else {
          alert(`Cancellation failed: ${response.message}`);
        }
      },
      error: (err) => {
        console.error('Error during cancellation:', err);
        alert('An error occurred while trying to cancel the order.');
      }
    });
  }

  closeEditProfile(): void {
    this.showEditProfile = false;
  }

  saveProfile(): void {
    const addressChangeRequest: AddressChangeRequest = {
      email: this.authService.getUsername(),
      oldAddress: this.userInfo.address,
      newAddress: this.editForm.address+", "+this.editForm.city
    }
    console.log(addressChangeRequest);

    this.authService.changeAddress(addressChangeRequest).subscribe({
      next: (messageResponse: MessageResponse) => {
        alert(messageResponse.message);
        this.closeEditProfile();
        window.location.reload();
      },
      error: (err) => {
        console.error('Greška pri promeni adrese:', err);
      }
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'completed':
        return 'status-completed';
      case 'pending':
        return 'status-pending';
      case 'preparing':
        return 'status-preparing';
      case 'cancelled':
        return 'status-cancelled';
      default:
        return '';
    }
  }

  getStatusIcon(status: string): string {
    switch (status) {
      case 'completed':
        return '✓';
      case 'pending':
        return '⏳';
      case 'preparing':
        return '👨‍🍳';
      case 'cancelled':
        return '✕';
      default:
        return '';
    }
  }

  formatDate(dateString: string): string {
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    };
    return new Date(dateString).toLocaleDateString('en-US', options);
  }

  openPromoCodeDialog(): void {
    this.showPromoCodeDialog = true;
    this.promoCodeForm.code = ''; // Resetovanje polja pri otvaranju
  }

  closePromoCodeDialog(): void {
    this.showPromoCodeDialog = false;
  }

  handlePromoCode(): void {
    const code = this.promoCodeForm.code;
    const email = this.authService.getUsername();

    if (!code || code.trim() === '') {
      alert('Please enter a valid code.');
      return;
    }

    // Poziv AuthService metode (pretpostavlja se da je ona već dodata)
    this.authService.addPromoCode(email, code).subscribe({
      next: (response: MessageResponse) => {
        alert(`Code submission status: ${response.message}`);
        this.closePromoCodeDialog();
      },
      error: (err) => {
        console.error('Greška pri dodavanju koda:', err);
        // Prikazivanje greške, npr. iz Angular ErrorResponse
        const errorMessage = err.error?.message || 'Failed to add promo code.';
        alert(`Error: ${errorMessage}`);
      }
    });
  }
}