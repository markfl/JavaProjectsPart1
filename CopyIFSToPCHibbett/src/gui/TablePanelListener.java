package gui;

public interface TablePanelListener {
	public void selectEventOccured(int row, String xmlName, String objectType);
	public void doubleClickEventOccured(int row, String xmlName, String objectType);
	public void rightClickEventOccured(int row, String xmlName, String objectType);
}
