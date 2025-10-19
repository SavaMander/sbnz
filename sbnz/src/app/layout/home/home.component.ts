import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  constructor(private authService: AuthService, private router: Router) { }
  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
  }
  registerForm = {
    username: '', 
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '', 
    address: '',
    creditCardNumber: '', 
    city: ''
  };

 
  loginForm = {
    email: '',
    password: ''
  };

  categories: Category[] = [
    { id: 'all', name: 'All', icon: '🍔' },
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


onlyNumbers(event: KeyboardEvent): void {
  const charCode = event.which ? event.which : event.keyCode;
  
  if (charCode < 48 || charCode > 57) {
    event.preventDefault();
  }
}


handleRegister(): void {
    
    if (this.registerForm.password !== this.registerForm.confirmPassword) {
      alert('passwords dont match');
      return;
    }

    
    if (!this.registerForm.username || !this.registerForm.email || !this.registerForm.phoneNumber ||
        !this.registerForm.address || !this.registerForm.creditCardNumber) {
      alert('Fill all fields');
      return;
    }
    
    
    const request: RegistrationRequest = {
      username: this.registerForm.username,
      email: this.registerForm.email,
      password: this.registerForm.password,
      passwordRepeat: this.registerForm.confirmPassword, 
      phoneNumber: this.registerForm.phoneNumber,
      address: this.registerForm.address+", "+this.registerForm.city,
      creditCardNumber: this.registerForm.creditCardNumber
    };

    
    this.authService.register(request).subscribe({
      next: (response) => {
        
        alert(response.message); 
        if (response.successful) {
          this.closeRegisterDialog();
          this.switchToLogin(); 
        }
      },
      error: (err) => {
        
        const errorMessage = err.error?.message || 'Failed to register';
        alert(errorMessage);
        console.error('Registration Error:', err);
      }
    });
  }

handleLogin(): void {
    
    const request: LoginRequest = {
      username: this.loginForm.email, 
      password: this.loginForm.password
    };

    
    this.authService.login(request).subscribe({
      next: (response: LoginResponse) => {
        
        localStorage.setItem('user', response.jwt); 
        this.authService.setUser(); 
        if(this.authService.getRole()=='ROLE_Admin'){
          this.router.navigate(["/admin"]);
        } else {
        this.closeLoginDialog();
        window.location.reload();
        }
        
      },
      error: (err) => {
        
        
        const errorMessage = err.error?.message || 'Invalida credentials or account is suspended';
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