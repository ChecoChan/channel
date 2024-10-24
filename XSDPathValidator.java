import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.Iterator;

public class XSDPathValidator {

    public static void main(String[] args) {
        File xsdFile = new File("C:/Users/HiuChung Chan/Desktop/pain.001.001.03.xsd");
        String[] elements = {"Document", "CstmrCdtTrfInitn", "GrpHdr", "CreDtTm"};
        boolean isValid = validatePathInXSD(xsdFile, elements);
        System.out.println("路径是否有效: " + isValid);
    }

    public static boolean validatePathInXSD(File xsdFile, String[] elements) {
        try {
            // 解析 XSD 文件
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xsdDocument = dBuilder.parse(xsdFile);

            // 初始化 XPath
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new NamespaceContext() {
                public String getNamespaceURI(String prefix) {
                    if (prefix.equals("xs")) {
                        return "http://www.w3.org/2001/XMLSchema";
                    }
                    return null;
                }

                public String getPrefix(String namespaceURI) {
                    return null;
                }

                public Iterator<String> getPrefixes(String namespaceURI) {
                    return null;
                }
            });

            // 从根元素开始验证路径
            return validateElementsRecursive(xsdDocument, xPath, elements, 0, null);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean validateElementsRecursive(Node currentNode, XPath xPath, String[] elements, int index, String parentType) throws Exception {
        if (index >= elements.length) {
            return true;
        }

        String elementName = elements[index];
        Node elementNode = findElementByName(currentNode, xPath, elementName, parentType);

        if (elementNode == null) {
            return false; // 找不到当前元素
        }

        // 获取 type 属性以继续递归
        String type = getTypeFromElement(elementNode);
        if (type == null) {
            return false; // 没有类型定义，路径无效
        }

        // 递归验证子元素
        return validateElementsRecursive(currentNode, xPath, elements, index + 1, type);
    }

    // 查找当前节点下是否存在给定名称的元素
    private static Node findElementByName(Node currentNode, XPath xPath, String elementName, String parentType) throws Exception {
        String expression;
        if (parentType != null) {
            // 如果有父元素类型，查找对应 complexType 中的子元素
            expression = "//xs:complexType[@name='" + parentType + "']//xs:element[@name='" + elementName + "']";
        } else {
            // 没有父元素时，查找根元素
            expression = "//xs:element[@name='" + elementName + "']";
        }

        XPathExpression expr = xPath.compile(expression);
        return (Node) expr.evaluate(currentNode, XPathConstants.NODE);
    }

    // 获取元素的 type 属性
    private static String getTypeFromElement(Node elementNode) {
        if (elementNode.getAttributes() != null && elementNode.getAttributes().getNamedItem("type") != null) {
            return elementNode.getAttributes().getNamedItem("type").getNodeValue();
        }
        return null;
    }
}
