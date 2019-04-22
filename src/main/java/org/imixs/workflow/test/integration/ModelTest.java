package org.imixs.workflow.test.integration;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.parsers.ParserConfigurationException;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.Model;
import org.imixs.workflow.bpmn.BPMNModel;
import org.imixs.workflow.bpmn.BPMNParser;
import org.imixs.workflow.engine.ModelService;
import org.imixs.workflow.exceptions.ModelException;
import org.imixs.workflow.exceptions.QueryException;
import org.xml.sax.SAXException;

/**
 * The ModelTest loads a BPMN model
 * 
 * @version 1.0
 * @author rsoika
 * 
 */
@Stateless
public class ModelTest implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ModelTest.class.getName());

	@EJB
	ModelService modelService;

	
	public boolean run() throws QueryException, ModelException {

		loadModel("/workflow/ticket-1.0.0.bpmn");
		
		// test model
		Model model = modelService.getModel("ticket-1.0");
		
		ItemCollection task=model.getTask(1000);
		if (task==null) {
			return false;
		}
		
		return true;
	}

	/**
	 * loads the current model
	 */
	public void loadModel(String modelPath) {
		if (modelPath != null) {
			long lLoadTime = System.currentTimeMillis();
			InputStream inputStream = IntegrationTestService.class.getResourceAsStream(modelPath);
			try {
				logger.info("loading model: " + modelPath + "....");
				BPMNModel model = BPMNParser.parseModel(inputStream, "UTF-8");

				this.modelService.addModel(model);
				logger.fine("...loadModel processing time=" + (System.currentTimeMillis() - lLoadTime) + "ms");
			} catch (ModelException | ParseException | ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}

		}

	}
}