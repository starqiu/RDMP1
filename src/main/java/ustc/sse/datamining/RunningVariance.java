package ustc.sse.datamining;

public final class RunningVariance {
	private int count;// 样本的个数
	private double mk;// 平均值
	private double sk;// Sn
	private double runVar;// 样本方差

	public RunningVariance() {
		this(0, 0.0, 0.0);
	}

	public RunningVariance(int count, double mk, double sk) {
		this.count = count;
		this.mk = mk;
		this.sk = sk;
		recomputeRunVar();
	}

	public double getMk() {
		return mk;
	}

	public double getSk() {
		return sk;
	}

	/**
	 * 获取运行时样本方差
	 * 
	 * @return
	 */
	public synchronized double getRunningVariance() {
		return runVar;
	}

	/**
	 * 增加样本
	 * 
	 * @param sample
	 */
	public synchronized void addSample(double sample) {
		if (++count == 1) {
			mk = sample;
			sk = 0.0;
		} else {
			double oldmk = mk;
			double diff = sample - oldmk;
			mk += diff / count;
			sk += diff * (sample - mk);
		}
		recomputeRunVar();
	}

	/**
	 * 移除样本
	 * 
	 * @param sample
	 */
	public synchronized void removeSample(double sample) {
		int oldCount = getCount();
		double oldmk = mk;
		if (oldCount == 0) {
			throw new IllegalStateException();
		}
		if (--count == 0) {
			mk = Double.NaN;
			sk = Double.NaN;
		} else {
			mk = (oldCount * oldmk - sample) / (oldCount - 1);
			sk -= (sample - mk) * (sample - oldmk);
		}
		recomputeRunVar();
	}

	private synchronized void recomputeRunVar() {
		int count = getCount();
		runVar = count > 1 ? sk / (count - 1) : Double.NaN;
		// 若需要计算标准差
		// runVar = count > 1 ? Math.sqrt(sk / (count - 1)) : Double.NaN;
	}

	public synchronized int getCount() {
		return count;
	}
}