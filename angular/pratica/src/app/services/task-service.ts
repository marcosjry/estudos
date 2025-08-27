import { effect, Injectable, signal } from '@angular/core';
import { Task } from '../models/task.model';
import { BehaviorSubject, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  
  private tasks$ = new BehaviorSubject<Task[]>(this.loadFromLocalStorage());

  constructor() {
    this.tasks$.subscribe(tasks => {
      this.saveToLocalStorage(tasks);
    });
  }

  getTasks(): Observable<Task[]> {
    return this.tasks$.asObservable();
  }

  addTask(title: string) {
    if (!title.trim()) return;
    const newTask: Task = {
      id: crypto.randomUUID(),
      title: title,
      completed: false,
      createdAt: new Date(),
    };
    const currentTasks = this.tasks$.getValue();
    this.tasks$.next([...currentTasks, newTask]);
  }

  deleteTask(id: string) {
    const currentTasks = this.tasks$.getValue();
    const updatedTasks = currentTasks.filter(task => task.id !== id);
    this.tasks$.next(updatedTasks);
  }

  toggleTaskCompletion(id: string) {
    const currentTasks = this.tasks$.getValue();
    const updatedTasks = currentTasks.map(task =>
      task.id === id ? { ...task, completed: !task.completed } : task
    );
    this.tasks$.next(updatedTasks);
  }

  getTaskById(id: string): Observable<Task | undefined> {
    return this.getTasks().pipe(
      map(tasks => tasks.find(task => task.id === id))
    );
  }

  private saveToLocalStorage(tasks: Task[]) {
    if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.setItem('angular-tasks', JSON.stringify(tasks));
    }
  }

  private loadFromLocalStorage(): Task[] {
    if (typeof window !== 'undefined' && window.localStorage) {
        const savedTasks = localStorage.getItem('angular-tasks');
        return savedTasks ? JSON.parse(savedTasks, (key, value) => {
            if (key === 'createdAt') {
                return new Date(value);
            }
            return value;
        }) : [];
    }
    return [];
  }

}
