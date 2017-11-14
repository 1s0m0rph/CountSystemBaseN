
/**
 * kNum object, used by BaseN for its digits. (BaseN numbers are collections of kNums)
 * 
 * @author Daniel Taylor (Ambulator)
 * @version 13.11.17
 */
public class kNum
{
    String subscript;
    public kNum(String ss)
    {
        subscript = ss;
    }
    
    String getSubscript(){return subscript;}
    void setSubscript(String set){subscript = set;}
}
