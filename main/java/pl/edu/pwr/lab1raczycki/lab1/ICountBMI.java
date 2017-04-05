package pl.edu.pwr.lab1raczycki.lab1;

/**
 * Created by RaczeQ on 2017-03-15.
 */

public interface ICountBMI {
    boolean isMassValid(float mass);
    boolean isHeightValid(float height);
    float countBMI(float mass, float height);
}
