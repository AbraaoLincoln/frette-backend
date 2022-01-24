package br.com.fretee.freteebackend.usuarios.helpers;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DistanceCalculator {
    public double calculateDistanceInKilometer(double userLat, double userLng,
                                                double venueLat, double venueLng) {
        final  double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        //return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }

    public Double formatarDouble(double valor, int precisao) {
        Double doubleFormatado = BigDecimal.valueOf(valor)
                .setScale(precisao, RoundingMode.HALF_UP)
                .doubleValue();

        return doubleFormatado;
    }
}
