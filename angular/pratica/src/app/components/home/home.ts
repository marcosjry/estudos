import { Component } from '@angular/core';
import { TaskList } from "../task-list/task-list";

@Component({
  selector: 'app-home',
  imports: [
    TaskList
],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {}
