package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Branch;
import org.dom4j.Comment;
import org.dom4j.ContentFactory;
import org.dom4j.Element;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.io.XMLWriter;

/** <p><code>AbstractBranch</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractBranch extends AbstractNode implements Branch {

    /** The XML writer used by default */
    protected static final XMLWriter writer = new XMLWriter( "  ", true );

    /** The <code>ContentFactory</code> instance used by default */
    private static final ContentFactory CONTENT_FACTORY = ContentFactory.getInstance();

    
    public AbstractBranch() { 
    }
 
    // Content Model methods
    
    public String getText() {
        return getContentModel().getText();
    }

    public String getTextTrim() {
        return getContentModel().getTextTrim();
    }

    public List getProcessingInstructions() {
        return getContentModel().getProcessingInstructions();
    }
    
    public List getProcessingInstructions(String target) {
        return getContentModel().getProcessingInstructions(target);
    }
    
    public ProcessingInstruction getProcessingInstruction(String target) {
        return getContentModel().getProcessingInstruction(target);
    }
    
    public void setProcessingInstructions(List listOfPIs) {
        getContentModel().setProcessingInstructions(listOfPIs);
    }
    
    public boolean removeProcessingInstruction(String target) {
        return getContentModel().removeProcessingInstruction(target);
    }
       
    public List getContent() {
        return getContentModel().getContent();
    }
    
    public void setContent(List content) {
        getContentModel().setContent(content);
    }
    
    public void clearContent() {
        getContentModel().clearContent();
    }
    
    public Node getNode(int index) {
        return getContentModel().getNode(index);
    }
    
    public int getNodeCount() {
        return getContentModel().getNodeCount();
    }
    
    public Iterator nodeIterator() {
        return getContentModel().nodeIterator();
    }
    
    public Comment addComment(String comment) {
        Comment node = getContentModel().addComment(getContentFactory(), comment);
        childAdded(node);
        return node;
    }
    
    public Element addElement(String name) {
        Element node = getContentModel().addElement(getContentFactory(), name);
        childAdded(node);
        return node;
    }
    
    public Element addElement(String name, String prefix, String uri) {
        Element node = getContentModel().addElement(getContentFactory(), name, prefix, uri);
        childAdded(node);
        return node;
    }
    
    public Element addElement(String name, Namespace namespace) {
        Element node = getContentModel().addElement(getContentFactory(), name, namespace);
        childAdded(node);
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(String target, String data) {
        ProcessingInstruction node = getContentModel().addProcessingInstruction(getContentFactory(), target, data);
        childAdded(node);
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(String target, Map data) {
        ProcessingInstruction node = getContentModel().addProcessingInstruction(getContentFactory(), target, data);
        childAdded(node);
        return node;
    }
    

    // typesafe versions using node classes
    public void add(Comment comment) {
        addNode(comment);
    }
    
    public void add(Element element) {
        addNode(element);
    }
    
    public void add(ProcessingInstruction pi) {
        addNode(pi);
    }
    
    public boolean remove(Comment comment) {
        return removeNode(comment);
    }
    
    public boolean remove(Element element) {
        return removeNode(element);
    }
    
    public boolean remove(ProcessingInstruction pi) {
        return removeNode(pi);
    }
    
    
    
    
    // Implementation methods
    
    protected void addNode(Node node) {
        if (node.getParent() != null) {
            // XXX: could clone here
            String message = "The Node already has an existing parent of \"" 
                + node.getParent().getQualifiedName() + "\"";
            throw new IllegalAddNodeException(this, node, message);
        }
        getContentModel().addNode(node);
        childAdded(node);
    }

    protected boolean removeNode(Node node) {
        boolean answer = getContentModel().removeNode(node);
        if (answer) {
            childRemoved(node);
        }
        return answer;
    }
    
    
    /** Allows derived classes to override the factory behaviour */
    protected ContentFactory getContentFactory() {
        return CONTENT_FACTORY;
    }

    /** Called when a new child node has been added to me
      * to allow any parent relationships to be created or
      * events to be fired.
      */
    protected abstract void childAdded(Node node);
    
    /** Called when a child node has been removed 
      * to allow any parent relationships to be deleted or
      * events to be fired.
      */
    protected abstract void childRemoved(Node node);
    
    /** Allows derived classes to override the content model */
    protected abstract ContentModel getContentModel();
    
}