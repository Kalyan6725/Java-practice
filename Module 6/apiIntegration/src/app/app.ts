import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ShowEmployees } from "./components/show-employees/show-employees";
import { EmployeeDashboardComponent } from "./components/employee-dashboard-component/employee-dashboard-component";
import { NavbarComponent } from "./components/navbar-component/navbar-component";
import { AddEmployee } from "./components/add-employee/add-employee";

@Component({
  selector: 'app-root',
  imports: [ShowEmployees, EmployeeDashboardComponent, NavbarComponent, RouterOutlet, AddEmployee],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
