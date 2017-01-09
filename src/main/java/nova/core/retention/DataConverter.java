package nova.core.retention;

/**
 * @author soniex2
 */
public interface DataConverter {
	Object fromData(Data d);

	void toData(Object o, Data data);
}
