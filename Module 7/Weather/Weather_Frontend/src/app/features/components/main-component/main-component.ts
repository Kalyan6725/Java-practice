import { Component } from '@angular/core';
import { Navbar } from "../navbar/navbar";
import { RouterOutlet } from "@angular/router";
import { Footer } from "../footer/footer";

@Component({
  selector: 'app-main-component',
  imports: [Navbar, RouterOutlet, Footer],
  templateUrl: './main-component.html',
  styleUrl: './main-component.css',
})
export class MainComponent {}
