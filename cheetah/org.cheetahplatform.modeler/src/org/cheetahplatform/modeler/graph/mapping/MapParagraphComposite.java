package org.cheetahplatform.modeler.graph.mapping;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.swtdesigner.SWTResourceManager;

public class MapParagraphComposite extends Composite {
	private Composite paragraphsComposite;
	private Tree tree;
	private TreeViewer commandsViewer;
	private Composite composite_2;
	private Button forwardButton;
	private Composite edgeConditionsComposite;
	private Label scoreLabel;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MapParagraphComposite(Composite parent, int style) {
		super(parent, style);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gridLayout = new GridLayout(3, false);
		setLayout(gridLayout);
		{
			Label lblEdgeConditions = new Label(this, SWT.NONE);
			lblEdgeConditions.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			lblEdgeConditions.setText("Edge Conditions");
		}
		{
			Label lblParagraphs = new Label(this, SWT.NONE);
			lblParagraphs.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			lblParagraphs.setText("Paragraphs");
		}
		{
			Composite composite_1 = new Composite(this, SWT.NONE);
			composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
			TreeColumnLayout treeColumnLayout = new TreeColumnLayout();
			composite_1.setLayout(treeColumnLayout);
			{
				commandsViewer = new TreeViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
				tree = commandsViewer.getTree();
				tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				tree.setSize(343, 272);
				tree.setHeaderVisible(true);
				tree.setLinesVisible(true);
				{
					TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
					treeColumn.setText("#");
					treeColumnLayout.setColumnData(treeColumn, new ColumnWeightData(10));
				}
				{
					TreeColumn trclmnTimestamp = new TreeColumn(tree, SWT.NONE);
					treeColumnLayout.setColumnData(trclmnTimestamp, new ColumnWeightData(20));
					trclmnTimestamp.setText("Timestamp");
				}
				{
					TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
					treeColumn.setText("Command");
					treeColumnLayout.setColumnData(treeColumn, new ColumnWeightData(40));
				}
				{
					TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
					treeColumn.setText("State");
					treeColumnLayout.setColumnData(treeColumn, new ColumnWeightData(15));
				}
				{
					TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
					treeColumn.setText("Paragraph");
					treeColumnLayout.setColumnData(treeColumn, new ColumnWeightData(30));
				}
			}
		}
		{
			edgeConditionsComposite = new Composite(this, SWT.NONE);
			GridData gd_edgeConditionsComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
			gd_edgeConditionsComposite.widthHint = 50;
			edgeConditionsComposite.setLayoutData(gd_edgeConditionsComposite);
			GridLayout gl_edgeConditionsComposite = new GridLayout(1, false);
			gl_edgeConditionsComposite.marginHeight = 0;
			gl_edgeConditionsComposite.marginWidth = 0;
			gl_edgeConditionsComposite.verticalSpacing = 0;
			gl_edgeConditionsComposite.horizontalSpacing = 0;
			edgeConditionsComposite.setLayout(gl_edgeConditionsComposite);
		}
		{
			paragraphsComposite = new Composite(this, SWT.NONE);
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
			layoutData.widthHint = 50;
			paragraphsComposite.setLayoutData(layoutData);
			GridLayout gridLayout_1 = new GridLayout(1, false);
			gridLayout_1.verticalSpacing = 0;
			gridLayout_1.marginWidth = 0;
			gridLayout_1.marginHeight = 0;
			gridLayout_1.horizontalSpacing = 0;
			paragraphsComposite.setLayout(gridLayout_1);
		}

		composite_2 = new Composite(this, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, true));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		{
			forwardButton = new Button(composite_2, SWT.NONE);
			forwardButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			forwardButton.setText("Next Manual Intervention");
		}
		new Label(composite_2, SWT.NONE);
		scoreLabel = new Label(this, SWT.NONE);
		scoreLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		scoreLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the commandsViewer
	 */
	public TreeViewer getCommandsViewer() {
		return commandsViewer;
	}

	public Composite getEdgeConditionsComposite() {
		return edgeConditionsComposite;
	}

	/**
	 * @return the forwardButton
	 */
	public Button getForwardButton() {
		return forwardButton;
	}

	/**
	 * @return the paragraphsCommand
	 */
	public Composite getParagraphsComposite() {
		return paragraphsComposite;
	}

	protected Label getScoreLabel() {
		return scoreLabel;
	}

}
