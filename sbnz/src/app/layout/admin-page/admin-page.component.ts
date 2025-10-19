import { Component, OnInit } from '@angular/core';
import { Profile } from 'src/app/dto/Profile';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {
  profiles: Profile[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  selectedStatusFilter: string = 'all';
  selectedSuspicionFilter: string = 'all';
  searchQuery: string = '';
  sortBy: string = 'username';

  showUserDetailDialog: boolean = false;
  selectedUser: Profile | null = null;
  showEditDialog: boolean = false;

  editForm: Partial<Profile> = {};

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.getUsers().subscribe({
      next: (data: Profile[]) => {
        this.profiles = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.errorMessage = 'Failed to load users. Please try again later.';
        this.isLoading = false;
      }
    });
  }

  openUserDetail(user: Profile): void {
    this.selectedUser = user;
    this.showUserDetailDialog = true;
  }

  closeUserDetail(): void {
    this.showUserDetailDialog = false;
    this.selectedUser = null;
  }

  suspendUser(user: Profile): void {
    // if (confirm(`Are you sure you want to suspend user ${user.username}?`)) {
    //   const index = this.profiles.findIndex(p => p.id === user.id);
    //   if (index !== -1) {
    //     this.profiles[index].accountStatus = 'suspended';
    //     alert(`User ${user.username} has been suspended.`);
    //     this.closeUserDetail();
    //   }
    // }
  }

  getSuspicionColor(level?: string): string {
    if (!level) return '';
    switch (level) {
      case 'VALID':
        return 'suspicion-low';
      case 'SUSPICIOUS':
        return 'suspicion-medium';
      case 'MALICIOUS':
        return 'suspicion-high';
      default:
        return '';
    }
  }

  formatDate(dateString?: string): string {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }
}