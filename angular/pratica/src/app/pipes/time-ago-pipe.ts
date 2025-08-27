import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeAgo'
})
export class TimeAgoPipe implements PipeTransform {

  transform(value: Date): string {
    if (!value) return '';
    
    const now = new Date();
    const seconds = Math.floor((now.getTime() - new Date(value).getTime()) / 1000);

    if (seconds < 29) return 'agora mesmo';

    const intervals: { [key: string]: number } = {
      'ano': 31536000,
      'mês': 2592000,
      'semana': 604800,
      'dia': 86400,
      'hora': 3600,
      'minuto': 60,
      'segundo': 1
    };

    let counter;
    for (const i in intervals) {
      counter = Math.floor(seconds / intervals[i]);
      if (counter > 0) {
        if (counter === 1) {
          return `há ${counter} ${i}`;
        } else {
          const plural = i === 'mês' ? 'meses' : `${i}s`;
          return `há ${counter} ${plural}`;
        }
      }
    }
    return value.toLocaleDateString();
  }

}
