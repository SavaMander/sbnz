import { Component, OnInit } from '@angular/core';
import { OrderRequest } from 'src/app/dto/OrderRequest';
import { AuthService } from 'src/app/services/auth.service';
import { OrderService } from 'src/app/services/order.service';

interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
  emoji: string;
}

interface CartItem extends MenuItem {
  quantity: number;
}

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.css']
})
export class RestaurantComponent implements OnInit {
  cart: CartItem[] = [];
  cartTotal: number = 0;
  cartItemCount: number = 0;
  restaurantName: string = 'Pizza Palace';
  isLoggedIn = false;
  role = '';
  private SUSPICIOUS_IPS: string[] = [
        '192.168.1.100',
        '10.0.0.5',
        '172.16.20.1',
        '185.120.10.15',
        '45.70.80.90'
    ];

  pizzas: MenuItem[] = [
    {
      id: 1,
      name: 'Margherita',
      description: 'Fresh mozzarella, basil, and tomato sauce',
      price: 1200,
      emoji: '🍕'
    },
    {
      id: 2,
      name: 'Pepperoni',
      description: 'Classic pepperoni and melted cheese',
      price: 1300,
      emoji: '🍕'
    },
    {
      id: 3,
      name: 'Vegetarian',
      description: 'Bell peppers, mushrooms, onions, olives',
      price: 1000,
      emoji: '🍕'
    },
    {
      id: 4,
      name: 'Quattro Formaggi',
      description: 'Four cheese blend pizza',
      price: 1600,
      emoji: '🍕'
    }
  ];

  sides: MenuItem[] = [
    {
      id: 5,
      name: 'Garlic Bread',
      description: 'Crispy bread with garlic butter',
      price: 600,
      emoji: '🍟'
    },
    {
      id: 6,
      name: 'Caesar Salad',
      description: 'Fresh greens with parmesan and croutons',
      price: 800,
      emoji: '🥗'
    },
    {
      id: 7,
      name: 'Pasta Carbonara',
      description: 'Creamy pasta with bacon and cheese',
      price: 1100,
      emoji: '🍝'
    },
    {
      id: 8,
      name: 'Calamari Fritti',
      description: 'Fried squid with marinara sauce',
      price: 1050,
      emoji: '🥒'
    }
  ];

  drinks: MenuItem[] = [
    {
      id: 9,
      name: 'Coca Cola',
      description: 'Ice cold soft drink',
      price: 150,
      emoji: '🥤'
    },
    {
      id: 10,
      name: 'Fresh Orange Juice',
      description: 'Freshly squeezed juice',
      price: 250,
      emoji: '🧃'
    },
    {
      id: 11,
      name: 'Italian Wine',
      description: 'Premium red wine',
      price: 2000,
      emoji: '🍷'
    },
    {
      id: 12,
      name: 'Iced Tea',
      description: 'Refreshing iced tea',
      price: 150,
      emoji: '🧋'
    }
  ];

  constructor(private orderService: OrderService, private authService: AuthService) {
  }
  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.role = this.authService.getRole();
  }

  addToCart(item: MenuItem, quantity: number = 1): void {
    // Check if item already exists in cart
    const existingItem = this.cart.find(cartItem => cartItem.id === item.id);

    if (existingItem) {
      // If item exists, increase quantity
      existingItem.quantity += quantity;
    } else {
      // If item doesn't exist, add it to cart
      this.cart.push({
        ...item,
        quantity: quantity
      });
    }

    this.updateCartTotal();
  }

  removeFromCart(itemId: number): void {
    this.cart = this.cart.filter(item => item.id !== itemId);
    this.updateCartTotal();
  }

  updateQuantity(itemId: number, quantity: number): void {
    const item = this.cart.find(cartItem => cartItem.id === itemId);
    if (item) {
      if (quantity <= 0) {
        this.removeFromCart(itemId);
      } else {
        item.quantity = quantity;
        this.updateCartTotal();
      }
    }
  }

  private generateRandomIp(): string {
        const randomIndex = Math.floor(Math.random() * this.SUSPICIOUS_IPS.length);
        return this.SUSPICIOUS_IPS[randomIndex];
    }

    private formatOrderList(): string {
        return this.cart.map(item => `${item.quantity} x ${item.name}`).join(', ');
    }

  updateCartTotal(): void {
    this.cartTotal = 0;
    this.cartItemCount = 0;

    this.cart.forEach(item => {
      this.cartTotal += item.price * item.quantity;
      this.cartItemCount += item.quantity;
    });

    // Round to 2 decimal places
    this.cartTotal = Math.round(this.cartTotal * 100) / 100;
  }

  clearCart(): void {
    this.cart = [];
    this.updateCartTotal();
  }

  proceedToCheckout(): void {
        if (this.cart.length === 0) {
            alert('Cart is empty!');
            return;
        }

        // 1. Priprema DTO-a
        const orderRequest: OrderRequest = {
            email: this.authService.getUsername(), // Dohvatanje emaila (koji je username) iz Auth Service-a
            restaurant: this.restaurantName,
            orderList: this.formatOrderList(), // Konkatenacija stavki
            orderPrice: this.cartTotal,
            ipAddress: this.generateRandomIp() // Nasumična IP adresa
        };
        console.log(orderRequest);
        // 2. Slanje porudžbine
        this.orderService.order(orderRequest).subscribe({
            next: (response) => {
                alert(`${response.message}`);
                this.clearCart();
            },
            error: (err) => {
                const errorMessage = err.error?.message || 'Greška pri slanju porudžbine.';
                alert(errorMessage);
                console.error('Order Error:', err);
            }
        });
    }
}