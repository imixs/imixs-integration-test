package org.imixs.workflow.test.integration;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.engine.DocumentService;
import org.imixs.workflow.exceptions.QueryException;

/**
 * The LuceneTest simulates lucene updates and search queries
 * 
 * 
 * @version 1.0
 * @author rsoika
 * 
 */
@Stateless
public class LuceneTest implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(LuceneTest.class.getName());

	@EJB
	DocumentService documentService;

	public void run() throws QueryException {

		// test simple doc
		ItemCollection doc = createDocument();
		findDocument();
		documentService.remove(doc);

		// test save two times..
		doc = saveDocumentTwice();
		findDocument();
		documentService.remove(doc);
	}

	/**
	 * Creates a new document
	 * 
	 * @throws QueryException
	 */

	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public ItemCollection createDocument() {

		ItemCollection doc = new ItemCollection();
		doc.setItemValue("type", "test");
		doc.setItemValue("txtname", "some name");

		doc = documentService.save(doc);
		logger.info(" test document created");
		return doc;

	}

	/**
	 * This test simulates the situation where a document is saved twice within the
	 * same transaction. We expect only one lucene update event. But in the second
	 * save call the existing event is detected in the EventLogService method and the
	 * $modified time stamp of the event is updated.
	 * 
	 * @throws QueryException
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public ItemCollection saveDocumentTwice() {

		ItemCollection doc = new ItemCollection();
		doc.setItemValue("type", "test");
		doc.setItemValue("txtname", "test-case-2");

		doc = documentService.save(doc);
		doc = documentService.save(doc);
		logger.info(" test document saved two times");
		return doc;

	}

	/**
	 * Creates a new document
	 * 
	 * @throws QueryException
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public void findDocument() throws QueryException {

		// try to find the document again

		String query = "txtname:\"some name\"";

		List<ItemCollection> result = documentService.find(query, 10, 0);

		logger.info(" search test : " + result.size() + " documents found");

	}
}