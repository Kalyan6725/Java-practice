import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-pipes',
  imports: [CommonModule],
  templateUrl: './pipes.html',
  styleUrl: './pipes.css',
})
export class Pipes {
  title:string = 'Pipes in Angular';
  today:Date = new Date();
  salary:number = 123456.789;
}
