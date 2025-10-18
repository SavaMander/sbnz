import { Component, OnInit } from '@angular/core';
import { LoginRequest } from 'src/app/dto/LoginRequest';
import { LoginResponse } from 'src/app/dto/LoginResponse';
import { RegistrationRequest } from 'src/app/dto/RegistrationRequest';
import { AuthService } from 'src/app/services/auth.service';

interface Category {
  id: string;
  name: string;
  icon: string;
}

interface Restaurant {
  id: number;
  name: string;
  rating: number;
  reviews: number;
  deliveryTime: string;
  deliveryFee: string;
  image: string;
  badge: string;
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  searchQuery: string = '';
  selectedCategory: string = 'all';
  showRegisterDialog: boolean = false;
  showLoginDialog: boolean = false;
  isLoggedIn = false;
  constructor(private authService: AuthService) { }
  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
  }
  // Registration form data
  registerForm = {
    username: '', // Mora se poklopiti sa DTO-om
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '', // Ažurirano
    address: '',
    creditCardNumber: '', // Ažurirano,
    city: ''
  };

  // Login form data
  loginForm = {
    email: '',
    password: ''
  };

  categories: Category[] = [
    { id: 'all', name: 'All', icon: '🍔' },
    { id: 'pizza', name: 'Pizza', icon: '🍕' },
    { id: 'burger', name: 'Burgers', icon: '🍔' },
    { id: 'sushi', name: 'Sushi', icon: '🍣' },
    { id: 'dessert', name: 'Dessert', icon: '🍰' }
  ];

  restaurants: Restaurant[] = [
    {
      id: 1,
      name: 'Pizza Palace',
      rating: 4.8,
      reviews: 320,
      deliveryTime: '25-35 min',
      deliveryFee: '$2.99',
      image: '🍕',
      badge: 'Featured'
    },
    {
      id: 2,
      name: 'Burger Bliss',
      rating: 4.6,
      reviews: 215,
      deliveryTime: '20-30 min',
      deliveryFee: '$1.99',
      image: '🍔',
      badge: 'Popular'
    },
    {
      id: 3,
      name: 'Sushi Express',
      rating: 4.9,
      reviews: 480,
      deliveryTime: '30-40 min',
      deliveryFee: '$3.99',
      image: '🍣',
      badge: 'Top Rated'
    },
    {
      id: 4,
      name: 'Sweet Treats',
      rating: 4.7,
      reviews: 195,
      deliveryTime: '15-25 min',
      deliveryFee: '$0.99',
      image: '🍰',
      badge: 'New'
    }
  ];

  selectCategory(categoryId: string): void {
    this.selectedCategory = categoryId;
  }

  openRegisterDialog(): void {
    this.showRegisterDialog = true;
  }

  closeRegisterDialog(): void {
    this.showRegisterDialog = false;
    this.registerForm = {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      phoneNumber: '',
      creditCardNumber: '',
      address: '',
      city:''
    };
  }

  openLoginDialog(): void {
    this.showLoginDialog = true;
  }

  closeLoginDialog(): void {
    this.showLoginDialog = false;
    this.loginForm = {
      email: '',
      password: ''
    };
  }

// Add this method to validate credit card and allow only numbers
onlyNumbers(event: KeyboardEvent): void {
  const charCode = event.which ? event.which : event.keyCode;
  // Allow only numbers (0-9)
  if (charCode < 48 || charCode > 57) {
    event.preventDefault();
  }
}

// Update handleRegister method to validate credit card
handleRegister(): void {
    // 1. Validacija lozinki
    if (this.registerForm.password !== this.registerForm.confirmPassword) {
      alert('passwords dont match');
      return;
    }

    // 2. Validacija polja
    if (!this.registerForm.username || !this.registerForm.email || !this.registerForm.phoneNumber ||
        !this.registerForm.address || !this.registerForm.creditCardNumber) {
      alert('Fill all fields');
      return;
    }
    
    // 3. Kreiranje DTO objekta (usklađivanje naziva polja)
    const request: RegistrationRequest = {
      username: this.registerForm.username,
      email: this.registerForm.email,
      password: this.registerForm.password,
      passwordRepeat: this.registerForm.confirmPassword, // Iako je DTO expects passwordRepeat, name je bio confirmPassword, ali je sada promenjen u DTO-u
      phoneNumber: this.registerForm.phoneNumber,
      address: this.registerForm.address+", "+this.registerForm.city,
      creditCardNumber: this.registerForm.creditCardNumber
    };

    // 4. Poziv AuthService-a
    this.authService.register(request).subscribe({
      next: (response) => {
        // Prikazuje poruku iz backend-a
        alert(response.message); 
        if (response.successful) {
          this.closeRegisterDialog();
          this.switchToLogin(); // Možda prebaciti na login formu nakon uspešne registracije
        }
      },
      error: (err) => {
        // Očekuje se da je err.error DTO tipa MessageResponse ako je tako implementirano na backendu
        const errorMessage = err.error?.message || 'Greška prilikom registracije. Pokušajte ponovo.';
        alert(errorMessage);
        console.error('Registration Error:', err);
      }
    });
  }

handleLogin(): void {
    // 1. Kreiranje DTO objekta
    const request: LoginRequest = {
      username: this.loginForm.email, // Koristimo email kao username
      password: this.loginForm.password
    };

    // 2. Poziv AuthService-a
    this.authService.login(request).subscribe({
      next: (response: LoginResponse) => {
        // 3. Čuvanje JWT tokena i ažuriranje stanja
        localStorage.setItem('user', response.jwt); // Token se čuva pod ključem 'user'
        this.authService.setUser(); // Ažurira se stanje u servisu (uloga, korisničko ime)
        this.closeLoginDialog();
        window.location.reload();
        // Moguća navigacija na dashboard: this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        // 4. Obrada grešaka
        // Pretpostavlja se da je greška 401/403, ili da vraća MessageResponse.
        const errorMessage = err.error?.message || 'Greška prilikom prijave: Proverite email/lozinku.';
        alert(errorMessage);
        console.error('Login Error:', err);
      }
    });
  }

  switchToLogin(): void {
    this.closeRegisterDialog();
    this.openLoginDialog();
  }

  switchToRegister(): void {
    this.closeLoginDialog();
    this.openRegisterDialog();
  }
}