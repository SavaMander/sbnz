import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginResponse } from '../dto/LoginResponse';
import { MessageResponse } from '../dto/MessageResponse';
import { RegistrationRequest } from '../dto/RegistrationRequest';
import { LoginRequest } from '../dto/LoginRequest';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { env } from '../env/env';
import {JwtHelperService} from '@auth0/angular-jwt'
import { Profile } from '../dto/Profile';
import { AddressChangeRequest } from '../dto/AddressChangeRequest';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private user$=new BehaviorSubject("");
  userState=this.user$.asObservable();

  constructor(private http: HttpClient) { 
    this.setUser();
   }
  
  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    skip: 'true',
  });
  
  login(auth:LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(env.apiHost+'auth/login',auth,{
      headers: this.headers,
    });
  }

  register(auth: RegistrationRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(env.apiHost+'auth/register',auth,{
      headers: this.headers,
    });
  }

    profile(email: string): Observable<Profile>{
        return this.http.get<Profile>(env.apiHost+'auth/profile?email='+email,{
    });
    }

    changeAddress(addressChangeRequest: AddressChangeRequest): Observable<MessageResponse> {
          return this.http.put<MessageResponse>(env.apiHost+'address/change',addressChangeRequest,{
    });
    }
    
    addPromoCode(email: string, code: string): Observable<MessageResponse> {
    let params = new HttpParams()
      .set('email', email)
      .set('code', code);
    return this.http.post<MessageResponse>(
      `${env.apiHost}auth/add-code`, 
      null, 
      { params: params }
    );
  }

  getUsers(): Observable<Profile[]> {
    return this.http.get<Profile[]>(`${env.apiHost}auth/users`);
  }

    getRole(): any {
    if(this.isLoggedIn()){
      const accessToken: any=localStorage.getItem('user');
      const jwtHelper=new JwtHelperService();
      return jwtHelper.decodeToken(accessToken).role[0].authority;
    }
    return null;
  }
  getUsername(): any {
    if(this.isLoggedIn()){
      const accessToken: any=localStorage.getItem('user');
      const jwtHelper=new JwtHelperService();
      return jwtHelper.decodeToken(accessToken).sub;
    }
  }
  isLoggedIn(): boolean {
    return localStorage.getItem('user') != null;
  }

  setUser(): void {
    this.user$.next(this.getRole());
  }
}
