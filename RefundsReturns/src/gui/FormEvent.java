package gui;
import java.util.EventObject;

public class FormEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	private String store;
	private String startDate;
	private String endDate;
	private String weekNumber;
	private boolean refunds;
	private boolean returns;
	private String saveToPath;
	
	public FormEvent(Object source) {	
		super(source);
	}
	
	public FormEvent(Object source, String store, String startDate, String endDate, String weekNumber, boolean refunds, 
					 boolean returns, String saveToPath) {	
		super(source);
		
		this.store = store;
		this.startDate = startDate;
		this.endDate = endDate;
		this.weekNumber = weekNumber;
		this.refunds = refunds;
		this.returns = returns;
		this.saveToPath = saveToPath;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(String weekNumber) {
		this.weekNumber = weekNumber;
	}

	public boolean isRefunds() {
		return refunds;
	}

	public void setRefunds(boolean refunds) {
		this.refunds = refunds;
	}

	public boolean isReturns() {
		return returns;
	}

	public void setReturns(boolean returns) {
		this.returns = returns;
	}

	public String getSaveToPath() {
		return saveToPath;
	}

	public void setSaveToPath(String saveToPath) {
		this.saveToPath = saveToPath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}