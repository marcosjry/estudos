import { TimeAgoPipe } from './time-ago-pipe';

describe('TimeAgoPipe', () => {
  
  const pipe = new TimeAgoPipe();

  it('cria uma instância', () => {
    expect(pipe).toBeTruthy();
  });

  it('deve retornar uma string vazia se o valor for nulo ou indefinido', () => {
    // @ts-ignore: Testando intencionalmente um tipo inválido
    expect(pipe.transform(null)).toBe('');
    // @ts-ignore: Testando intencionalmente um tipo inválido
    expect(pipe.transform(undefined)).toBe('');
  });

  it('deve retornar "agora mesmo" para datas com menos de 29 segundos', () => {
    const now = new Date();
    const justNow = new Date(now.getTime() - 10 * 1000);
    expect(pipe.transform(justNow)).toBe('agora mesmo');
  });

  it('deve retornar "há 1 minuto" para datas entre 1 e 2 minutos atrás', () => {
    const now = new Date();
    const oneMinuteAgo = new Date(now.getTime() - 1 * 60 * 1000);
    expect(pipe.transform(oneMinuteAgo)).toBe('há 1 minuto');
  });

  it('deve retornar "há 1 hora" para datas 1 hora atrás', () => {
    const now = new Date();
    const oneHourAgo = new Date(now.getTime() - 1 * 60 * 60 * 1000);
    expect(pipe.transform(oneHourAgo)).toBe('há 1 hora');
  });

  it('deve retornar "há 1 dia" para datas 1 dia atrás', () => {
    const now = new Date();
    const oneDayAgo = new Date(now.getTime() - 1 * 24 * 60 * 60 * 1000);
    expect(pipe.transform(oneDayAgo)).toBe('há 1 dia');
  });
  
  it('deve retornar "há 1 semana" para datas 1 semana atrás', () => {
    const now = new Date();
    const oneWeekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
    expect(pipe.transform(oneWeekAgo)).toBe('há 1 semana');
  });

  it('deve retornar "há 1 mês" para datas 1 mês atrás', () => {
    const now = new Date();
    // Usamos 30 dias como uma aproximação para 1 mês
    const oneMonthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);
    expect(pipe.transform(oneMonthAgo)).toBe('há 1 mês');
  });

  it('deve retornar "há 1 ano" para datas 1 ano atrás', () => {
    const now = new Date();
    const oneYearAgo = new Date(now.getTime() - 365 * 24 * 60 * 60 * 1000);
    expect(pipe.transform(oneYearAgo)).toBe('há 1 ano');
  });

});