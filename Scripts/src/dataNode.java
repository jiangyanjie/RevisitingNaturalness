
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class dataNode {
    public ASTNode node;
    public int label;
    public List<Integer> childrenLables = new ArrayList<>();
    public List<ASTNode> childrenNodes = new ArrayList<>();
    public boolean isLeaf = false;
    public String nodeType = "node_type";
    public String attachedStatementType = "null";
    public List<Integer> logicPoseList = new ArrayList<>();
    public int numberOfToken = 0;
    public int numberOfToken2 = 0;
}
