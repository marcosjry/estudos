import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { TaskDetail } from './components/task-detail/task-detail';
import { NotFound } from './components/not-found/not-found';

export const routes: Routes = [
    {
        path: '',
        component: Home,
        title: 'Página Inicial | To-Do App'
    },
    { 
        path: 'task/:id', 
        component: TaskDetail, 
        title: 'Detalhes da Tarefa' 
    },
    { 
        path: '**', 
        component: NotFound, 
        title: '404 - Não Encontrado' 
    }
];
