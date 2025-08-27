import { CommonModule, DatePipe, NgIf } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskService } from '../../services/task-service';
import { Highlight } from '../../directives/highlight';
import { BehaviorSubject, combineLatest, map, Observable } from 'rxjs';
import { Task } from '../../models/task.model';
import { TimeAgoPipe } from '../../pipes/time-ago-pipe';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-task-list',
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    Highlight, 
    TimeAgoPipe, 
    RouterLink,
    NgIf
  ],
  templateUrl: './task-list.html',
  styleUrl: './task-list.scss'
})
export class TaskList {
  taskService = inject(TaskService);
  private fb = inject(FormBuilder);
  taskForm: FormGroup;
  filter$ = new BehaviorSubject<'all' | 'completed' | 'pending'>('all');
  filteredTasks$: Observable<Task[]>;

  constructor() {
    this.taskForm = this.fb.group({ title: ['', [Validators.required, Validators.minLength(3)]] });
    this.filteredTasks$ = combineLatest([this.taskService.getTasks(), this.filter$]).pipe(
      map(([tasks, filter]) => {
        if (filter === 'completed') return tasks.filter(t => t.completed);
        if (filter === 'pending') return tasks.filter(t => !t.completed);
        return tasks;
      })
    );
  }

  handleAddTask() {
    if (this.taskForm.valid) {
      this.taskService.addTask(this.taskForm.value.title);
      this.taskForm.reset();
    }
  }

  setFilter(filter: 'all' | 'completed' | 'pending') { 
    this.filter$.next(filter); 
  }

  trackById(index: number, task: Task): string { 
    return task.id; 
  }
}
