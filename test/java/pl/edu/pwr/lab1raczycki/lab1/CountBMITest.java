package pl.edu.pwr.lab1raczycki.lab1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Created by RaczeQ on 2017-03-15.
 */

public class CountBMITest {
    @Test
    public void massUnder0IsInvalid() throws Exception {
        //GIVEN
        float test_mass = -1.0f;
        //WHEN
        CountBMIForMetric BMICounterMetric = new CountBMIForMetric();
        //THEN
        assertFalse(BMICounterMetric.isMassValid(test_mass));
    }

    @Test
    public void massOver250IsInvalid() throws Exception {
        //GIVEN
        float test_mass = 251.0f;
        //WHEN
        CountBMIForMetric BMICounterMetric = new CountBMIForMetric();
        //THEN
        assertFalse(BMICounterMetric.isMassValid(test_mass));
    }

    @Test
    public void heightUnderHalfIsInvalid() throws Exception {
        //GIVEN
        float test_height = 0.49f;
        //WHEN
        CountBMIForMetric BMICounterMetric = new CountBMIForMetric();
        //THEN
        assertFalse(BMICounterMetric.isHeightValid(test_height));
    }

    @Test
    public void heightOverTwoAndHalfIsInvalid() throws Exception {
        //GIVEN
        float test_height = 2.51f;
        //WHEN
        CountBMIForMetric BMICounterMetric = new CountBMIForMetric();
        //THEN
        assertFalse(BMICounterMetric.isHeightValid(test_height));
    }

    @Test
    public void BMICounterResultIsValid() throws Exception {
        //GIVEN
        float test_height = 1.85f;
        float test_mass = 60f;
        float value = 17.5f;
        //WHEN
        CountBMIForMetric BMICounterMetric = new CountBMIForMetric();
        //THEN
        assertEquals(value, BMICounterMetric.countBMI(test_mass, test_height), 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void BMICounterExceptionIsValid() throws Exception {
        //GIVEN
        float test_height = -1.0f;
        float test_mass = -1.0f;
        //WHEN
        CountBMIForMetric BMICounterMetric = new CountBMIForMetric();
        //THEN
        BMICounterMetric.countBMI(test_mass, test_height);
    }
}
