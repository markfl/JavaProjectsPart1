package gui;
import java.util.EventListener;

public interface FormListener extends EventListener{
	public void formEventOccurred(FormEvent e);
	public void exitEventOccured();
}
