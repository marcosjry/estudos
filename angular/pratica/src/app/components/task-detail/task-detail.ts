import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TaskService } from '../../services/task-service';
import { Observable } from 'rxjs';
import { Task } from '../../models/task.model';
import { CommonModule, NgIf } from '@angular/common';
import { TimeAgoPipe } from '../../pipes/time-ago-pipe';

@Component({
  selector: 'app-task-detail',
  imports: [
    CommonModule, 
    RouterLink, 
    TimeAgoPipe,
    NgIf
  ],
  templateUrl: './task-detail.html',
  styleUrl: './task-detail.scss'
})
export class TaskDetail {
  private route = inject(ActivatedRoute);
  private taskService = inject(TaskService);

  task$: Observable<Task | undefined>;

  constructor() {
    const taskId = this.route.snapshot.paramMap.get('id');
    this.task$ = this.taskService.getTaskById(taskId!);
  }
}
