package clima;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachingInvocationHandler implements InvocationHandler {

    private final Object target;
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public CachingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("getForecast")) {
            String city = (String) args[0];

            if (cache.containsKey(city)) {
                System.out.println(">>> Cache HIT! Retornando previsão para '" + city + "' do cache.");
                return cache.get(city);
            }

            System.out.println(">>> Cache MISS! Buscando previsão para '" + city + "' na fonte real.");

            Object result = method.invoke(target, args);

            cache.put(city, result);

            return result;
        }

        return method.invoke(target, args);
    }


}
