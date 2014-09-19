package mnc.beacon.survey;

public class kalman {

	/** @brief Noise variance estimation in percent. */
	private static double PERCENT_VAR = 0.05f;
	/** @brief Filter gain. */
	private static double GAIN = 0.8f;
	/** @brief Noise variance. */
	double[] m_noisevar;
	/** @brief Corrected/filtered value. */
	double[] m_corrected;
	/** @brief Predicted variance. */
	double[] m_predictedvar;
	/** @brief Observed value due to measurement. */
	double[] m_observed;
	/** @brief The Kalman gain. */
	double[] m_kalman;
	/** @brief The m_corrected variance. */
	double[] m_correctedvar;
	/** @brief The m_predicted value. */
	double[] m_predicted;

	/**
	 * @brief Initializes the filter with some initial values and defines the
	 *        dimension used.
	 * 
	 * @param _initialValues
	 *            The values used for initialization.
	 * @param _dimension
	 *            The dimension of the filter.
	 */
	public void init(double[] _initialValues, int _dimension) {

		m_noisevar = new double[_dimension];
		m_corrected = new double[_dimension];
		m_predictedvar = new double[_dimension];
		m_observed = new double[_dimension];
		m_kalman = new double[_dimension];
		m_correctedvar = new double[_dimension];
		m_predicted = new double[_dimension];

		for (int i = 0; i < _dimension; i++) {
			m_noisevar[i] = PERCENT_VAR;
		}

		m_predictedvar = m_noisevar;
		m_predicted = _initialValues;
	}

	/**
	 * @brief Updates the Kalman filter.
	 * 
	 * @param _observedValue
	 *            The value gained by measuring.
	 * @param _dtx
	 *            Not used for this filter.
	 */
	public void update(double[] _observedValue, double _dtx) {

		// if dimensions do not match throw an exception
		if (_observedValue.length != m_observed.length) {
			throw new RuntimeException("Array dimensions do not match");
		}

		m_observed = _observedValue;

		// compute the Kalman gain for each dimension
		for (int i = 0; i < m_kalman.length; i++) {
			m_kalman[i] = m_predictedvar[i]
					/ (m_predictedvar[i] + m_noisevar[i]);
		}

		// update the sensor prediction with the measurement for each dimension
		for (int i = 0; i < m_corrected.length; i++) {
			m_corrected[i] = GAIN * m_predicted[i] + (1.0f - GAIN)
					* m_observed[i] + m_kalman[i]
					* (m_observed[i] - m_predicted[i]);
		}

		// update the variance estimation
		for (int i = 0; i < m_correctedvar.length; i++) {
			m_correctedvar[i] = m_predictedvar[i] * (1.0f - m_kalman[i]);
		}

		// predict next variances and values
		m_predictedvar = m_correctedvar;
		m_predicted = m_corrected;
	}

	/**
	 * @brief Provides the caller with the filtered values since the last
	 *        update.
	 * 
	 * @return A double array storing the filtered values.
	 */
	public double[] getCorrectedValues() {
		return m_corrected;
	}

	/**
	 * @brief Serializes the filter data of the current state to a string.
	 * 
	 *        Each value is seperated by a pipe, so it can be parsed by any GUI
	 *        later.
	 * 
	 * @return A concatenated string carrying the current filter data.
	 */
	public String currentStateToString() {

		String temp = "";

		/*
		 * for(int i = 0; i < m_observed.length; i++){ temp += m_observed[i] +
		 * "|"; }
		 * 
		 * for(int i = 0; i < m_corrected.length; i++){ temp += m_corrected[i] +
		 * "|"; }
		 */
		temp = m_corrected[m_corrected.length - 1] + "|";

		return temp;
	}

}
