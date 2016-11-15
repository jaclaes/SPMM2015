package at.floxx.scrumify.productManager.gui;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Contains the Productmanager GUI.
 * @author Mathias Breuss
 *
 */
public class ProductManagerComposite extends org.eclipse.swt.widgets.Composite {
	private Group productListGroup;
	private ProductListComposite productList;
	private Group productAttributesGroup;
	private Label nameLabel;
	private Label descriptionLabel;
	private Text nameFieldText;
	private Text descriptionText;
	private Button updateButton;
	private Button addButton;
	private Button deleteButton;
	private Composite productActionComposite;
	
	

	/**
	 * @return productList
	 */
	public ProductListComposite getProductList() {
		return productList;
	}

	/**
	 * @return nameFieldText
	 */
	public Text getNameFieldText() {
		return nameFieldText;
	}

	/**
	 * @return descriptionText
	 */
	public Text getDescriptionText() {
		return descriptionText;
	}

	/**
	 * @return updateButton
	 */
	public Button getUpdateButton() {
		return updateButton;
	}

	/**
	 * @return addButton
	 */
	public Button getAddButton() {
		return addButton;
	}

	/**
	 * @return deleteButton
	 */
	public Button getDeleteButton() {
		return deleteButton;
	}

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	 * @param args 
	*/
	public static void main(String[] args) {
		showGUI();
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		ProductManagerComposite inst = new ProductManagerComposite(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**The Constructor.
	 * @param parent
	 * @param style
	 */
	public ProductManagerComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.numColumns = 2;
			this.setLayout(thisLayout);
			{
				productListGroup = new Group(this, SWT.NONE);
				GridLayout productListGroupLayout = new GridLayout();
				productListGroupLayout.makeColumnsEqualWidth = true;
				productListGroup.setLayout(productListGroupLayout);
				productListGroup.setText("Products:");
				GridData productListGroupLData = new GridData();
				productListGroupLData.verticalAlignment = GridData.FILL;
				productListGroupLData.horizontalAlignment = GridData.FILL;
				productListGroup.setLayoutData(productListGroupLData);
				{
					productList = new ProductListComposite(productListGroup, SWT.NONE);
					GridData productListLData = new GridData();
					productListLData.verticalAlignment = GridData.FILL;
					productListLData.grabExcessVerticalSpace = true;
					productListLData.horizontalAlignment = GridData.FILL;
					productListLData.widthHint = 150;
					productList.setLayoutData(productListLData);
				}
				
				productAttributesGroup = new Group(this, SWT.NONE);
				GridLayout productAttributesGroupLayout = new GridLayout();
				productAttributesGroupLayout.numColumns = 1;
				productAttributesGroup.setLayout(productAttributesGroupLayout);
				productAttributesGroup.setText("Product Attributes:");
				GridData productAttributesGroupLData = new GridData();
				productAttributesGroupLData.verticalAlignment = GridData.FILL;
				productAttributesGroupLData.horizontalAlignment = GridData.FILL;
				productAttributesGroup.setLayoutData(productAttributesGroupLData);
				{
					nameLabel = new Label(productAttributesGroup, SWT.NONE);
					nameLabel.setText("Name:");
					
					GridData nameTextLD = new GridData();
					nameTextLD.horizontalAlignment = GridData.FILL;
					nameTextLD.grabExcessHorizontalSpace = true;
					nameFieldText = new Text(productAttributesGroup, SWT.NONE);
					nameFieldText.setLayoutData(nameTextLD);
								
					descriptionLabel = new Label(productAttributesGroup, SWT.NONE);
					descriptionLabel.setText("Description:");
					
					GridData descriptionTextLD = new GridData();
					descriptionTextLD.horizontalAlignment = GridData.FILL;
					descriptionTextLD.verticalSpan = 4;
					descriptionTextLD.heightHint = 100;
					descriptionTextLD.widthHint = 200;
					descriptionText = new Text(productAttributesGroup, SWT.MULTI | SWT.WRAP);
					descriptionText.setSize(400, 300);
					descriptionText.setLayoutData(descriptionTextLD);
				}
				
				productActionComposite = new Composite(this, SWT.NONE);
				FillLayout productActionCompositeLayout = new FillLayout();
				productActionComposite.setLayout(productActionCompositeLayout);
				GridData productActionCompositeLData = new GridData();
				productActionCompositeLData.verticalAlignment = GridData.FILL;
				productActionCompositeLData.horizontalAlignment = GridData.FILL;
				productActionCompositeLData.horizontalSpan = 2;
				productActionComposite.setLayoutData(productActionCompositeLData);
				{
					updateButton = new Button(productActionComposite, SWT.NONE);
					updateButton.setText("Update");
					
					addButton = new Button(productActionComposite, SWT.NONE);
					addButton.setText("Add");
					
					deleteButton = new Button(productActionComposite, SWT.NONE);
					deleteButton.setText("Delete");
				}
			}


			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
