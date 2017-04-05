package pl.edu.pwr.lab1raczycki.lab1;

import android.util.Log;

/**
 * Created by RaczeQ on 2017-03-15.
 */

public class CountBMIForImperial implements ICountBMI {
    static final float MIN_MASS = 6.0f;
    static final float MAX_MASS = 40.0f;
    static final float MIN_HEIGHT = 1.6f;
    static final float MAX_HEIGHT = 8.2f;

    @Override
    public boolean isMassValid(float mass) {
        return mass >= MIN_MASS && mass <= MAX_MASS;
    }

    @Override
    public boolean isHeightValid(float height) {
        return height >= MIN_HEIGHT && height <= MAX_HEIGHT;
    }

    @Override
    public float countBMI(float mass, float height) {
        String error = "";
        if (!isMassValid(mass)) {
            error += "Mass is invalid\n";
        }
        if (!isHeightValid(height)) {
            error += "Height is invalid\n";
        }
        height = height / 3.28f;
        mass = mass * 6.35f;
        if (error.equals("")) {
            return mass / (height * height);
        } else {
            Log.e("error",error);
            throw new IllegalArgumentException(error);
        }
    }
}
