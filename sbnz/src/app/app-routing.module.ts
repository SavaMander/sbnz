import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './layout/home/home.component';
import { RestaurantComponent } from './layout/restaurant/restaurant.component';
import { ProfileComponent } from './layout/profile/profile.component';
import { AdminPageComponent } from './layout/admin-page/admin-page.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {component: HomeComponent, path: "home"},
  {component: RestaurantComponent, path: "restaurant"},
  {component: ProfileComponent, path: "profile"},
  {component: AdminPageComponent, path: "admin"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
