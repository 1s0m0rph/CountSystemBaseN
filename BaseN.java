
/**
 * The Base N main class. Unlike the CountSystem, BaseN is modeled as a data type (more like BigInteger)
 * 
 * @author Daniel Taylor (Ambulator)
 * @version 13.11.17
 */
import java.util.ArrayList;
public class BaseN
{
    static String BASE;
    static Decimal d;
    boolean isNegative;
    ArrayList<kNum> v;
    
    /*/
    constructor to initialize the number to a certain value
    
    @param base the number base (_10 String)
    @param r the subscript arraylist
    @param negative true if the number is < 0
    /*/
    public BaseN(String base, ArrayList<kNum> r, boolean negative)
    {
        BASE = base;
        v = r;
        d = new Decimal();
        isNegative = negative;
    }
    
    /*/
    standard constructor. Initializes base to base and everything else to zero
    
    @param base the number base to initialize to
    /*/
    public BaseN(String base)
    {
        BASE = base;
        d = new Decimal();
        v = new ArrayList<kNum>();
        v.add(new kNum("0"));
        isNegative = false;
    }
    
    /*/
    helper methods, should be fairly self-explanatory
    most are used to interact with other BaseN numbers
    /*/
    int getLength(){return v.size();}
    String subscriptAt(int index){return v.get(index).getSubscript();}
    boolean getIsNegative(){return isNegative;}
    private ArrayList<kNum> getRepresentation(){return v;}
    void deleteIndex(int index){v.remove(index);}
    String getBase(){return BASE;}
    
    void setSubscript(int index, String set)
    {
        v.get(index).setSubscript(set);
    }
    
    /*/
    print a representation of the number. Note that this is not the ONLY possible representation, just one possibility
    for example, the number 12345, in base N_120 would be printed as:
    k_(102)k_(105)
    /*/
    void print()
    {
        if(isNegative)System.out.print("-");
        for(kNum k : v)
        {
            System.out.print("k_(" + k.getSubscript() + ")");
        }
        System.out.println();
    }
    
    void increment()
    {
        increment(v.size()-1);
    }
    
    /*/
    adds one to the current value recursively
    note that it uses the same addition method as CountSystem, so you can call increment on the wrong digit
    
    @param digitPos the current digit to modify
    /*/
    void increment(int digitPos)
    {
        if(isNegative)
        {
            if(v.size() == 1 && subscriptAt(0).equals("1"))
            {
                setSubscript(0,"0");
                isNegative = false;
                return;
            }
            isNegative = false;
            decrement();
            isNegative = true;
            return;
        }
        
        if(v.get(digitPos).getSubscript().equals(d.decrement(BASE)))
        {
            v.get(digitPos).setSubscript("0");
            if(digitPos == 0)                                        //string is all alpha[BASE-1]
            {
                v.add(0,new kNum("1"));
                return;
            }
        }
        else
        {
            v.get(digitPos).setSubscript(d.increment(v.get(digitPos).getSubscript()));
            return;
        }
        
        increment(digitPos - 1);
    }
    
    void decrement()
    {
        decrement(v.size()-1);
    }
    
    /*/
    takes one from the current value recursively
    note that it uses the same addition method as CountSystem, so you can call decrement on the wrong digit
    
    @param digitPos the current digit to modify
    /*/
    void decrement(int digitPos)
    {
        if(v.size() == 1 && subscriptAt(0).equals("0"))
        {
            isNegative = true;
            return;
        }
        if(isNegative)
        {
            isNegative = false;
            increment();
            isNegative = true;
            return;
        }
        
        if(!v.get(digitPos).getSubscript().equals("0"))
        {
            v.get(digitPos).setSubscript(d.decrement(v.get(digitPos).getSubscript()));
            if(digitPos == 0 && v.get(digitPos).getSubscript().equals("0") && v.size() > 1)
            {
                v.remove(0);
            }
            return;
        }
        else
        {
            if(digitPos == 0 && v.size() > 1)
            {
                v.remove(0);
                return;
            }
            else
            {
                v.get(digitPos).setSubscript(d.decrement(BASE));
            }
        }
        
        decrement(digitPos-1);
    }
    
    /*/
    converts the current number to a _10 string
    note that if the number, when converted to _10, results in a string with length > INT_MAX, bad things will happen
    
    @return the converted number (_10 string)
    /*/
    String convertTo_10()
    {
        String exp = Integer.toString(v.size()-1);
        String r = "0";
        for(int pos = 0; pos < v.size(); pos++)
        {
            r = d.add(r,d.multiply(v.get(pos).getSubscript(),d.pow(BASE,exp)));
            exp = d.decrement(exp);
        }
        return r;
    }
    
    /*/
    take the base BASE log of a _10 number. Used in conversion
    
    @param num the number to take log_BASE() of
    @return the _10 string representation of the log
    /*/
    String log(String num)
    {
        String r = "0";
        while(d.lessThan(d.pow(BASE,r),num))
        {
            r = d.increment(r);
        }
        return d.decrement(r);
    }
    
    /*/
    convert a number from base 10 to base N
    
     This is about as efficient as I think it can be. I'm probably wrong though
      
      conversion in base n_9849812 of 6816514562465206789542098129754802198544129051976485016574012:
      k_(76937)k_(6261200)k_(7350587)k_(8192732)k_(8196700)k_(2609509)k_(1945458)k_(6427280)k_(2999964)
      
      which takes a few minutes to calculate
      
     @param num the number to convert into base N
    /*/
    void convert(String num)
    {
        v.clear();
        if(num.charAt(0) == '-')
        {
            isNegative = true;
            convert(num.substring(1));
            return;
        }
        
        for(String exp = log(num); !d.lessThan(exp,"0"); exp = d.decrement(exp))
        {
            String currentExp = d.pow(BASE,exp);
            num = d.subtract(num,currentExp);
            String c = "0";
            while(num.charAt(0) != '-')
            {
                num = d.subtract(num,currentExp);
                c = d.increment(c);
            }
            v.add(new kNum(c));
            num = d.add(num,currentExp);
        }
    }
    
    /*/
    check if the current number is the same as a given BaseN number b
    note that if the base is different (even if the numbers are technically equal) it will always return false
    
    @param b the number to check if current = 
    @return true of they're the same false if not
    /*/
    boolean equals(BaseN b)
    {
        if(BASE != b.getBase())return false;
        if(b.getLength() != getLength())return false;
        
        for(int i = 0; i < getLength(); i++)
        {
            if(b.subscriptAt(i) != subscriptAt(i))return false;
        }
        return true;
    }
    
    /*/
    check if current < b
    
    @param b the number to check if current <
    @return true of current < b, false if current >= b
    /*/
    boolean lessThan(BaseN b)
    {
        if(BASE != b.getBase())return false;
        
        if(equals(b))return false;
        if(v.size() < 2 && subscriptAt(0) == "0")return !b.getIsNegative();
        if(b.getLength() < 2 && b.subscriptAt(0) == "0")return isNegative;
        if(getLength() < b.getLength())return true;
        if(getLength() > b.getLength())return false;
        
        for(int check = 0; check < getLength(); check++)
        {
            if(d.lessThan(subscriptAt(check),b.subscriptAt(check)))return true;
            if(!subscriptAt(check).equals(b.subscriptAt(check)) && d.lessThan(b.subscriptAt(check),subscriptAt(check)))return false;
        }
        return false;
    }
    
    /*/
    take current + b (and set current to that value)
    
    @param b
    /*/
    void add(BaseN b)
    {
        if(BASE != b.getBase())return;
        BaseN a = new BaseN(BASE,new ArrayList<kNum>(v),isNegative);
        int posA = a.getLength()-1, posB = b.getLength()-1;
        v.clear();
        
        String indexSum = "0";
        while(posA >= 0 && posB >= 0)
        {
            indexSum = d.add(indexSum,d.add(a.subscriptAt(posA),b.subscriptAt(posB)));
            if(d.lessThan(indexSum,BASE))
            {
                v.add(0,new kNum(indexSum));
                indexSum = "0";
            }
            else
            {
                indexSum = d.subtract(indexSum,BASE);
                v.add(0,new kNum(indexSum));
                indexSum = "1";
                if(posA == 0 && posB == 0)v.add(0,new kNum("1"));
            }
            posA--;
            posB--;
        }
        
        if(!indexSum.equals("0") && posA >= 0)
        {
            int pl = a.getLength();
            a.increment(posA);
            if(pl < a.getLength())posA++;
        }
        
        if(!indexSum.equals("0") && posB >= 0)
        {
            int pl = b.getLength();
            b.increment(posB);
            if(pl < b.getLength())posB++;
        }
        
        while(posA >= 0)
        {
            v.add(0,new kNum(a.subscriptAt(posA)));
            posA--;
        }
        
        while(posB >= 0)
        {
            v.add(0,new kNum(b.subscriptAt(posB)));
            posB--;
        }
    }
    
    /*/
    take current - b (and set current to that value)
    
    @param b
    /*/
    void subtract(BaseN b)
    {
        if(BASE != b.getBase())return;
        BaseN a = new BaseN(BASE,new ArrayList<kNum>(v),isNegative);
        v.clear();
        if(a.lessThan(b))
        {
            b.subtract(a);
            isNegative = true;
            v = new ArrayList<kNum>(b.getRepresentation());
            b.add(a);
            return;
        }
        int posA = a.getLength() - 1, posB = b.getLength() - 1;
        
        String indexDiff = "0";
        while(posA >= 0 && posB >= 0)
        {
            indexDiff = d.add(indexDiff,d.subtract(a.subscriptAt(posA),b.subscriptAt(posB)));
            if(!d.lessThan(indexDiff,"0"))
            {
                v.add(0,new kNum(indexDiff));
                indexDiff = "0";
            }
            else
            {
                indexDiff = d.add(BASE,indexDiff);
                v.add(0,new kNum(indexDiff));
                indexDiff = "-1";
                if(posA == 0 && posB == 0)isNegative = true;
            }
            posA--;
            posB--;
        }
        
        if(!indexDiff.equals("0") && posA >= 0)
        {
            int lO = a.getLength();
            a.decrement(posA);
            if(a.subscriptAt(0) == "-")
            {
                a.setSubscript(1,d.decrement(BASE));
                a.deleteIndex(0);
            }
            if(lO > a.getLength())
            {
                posA--;
            }
        }
        
        if(!indexDiff.equals("0") && posB >= 0)
        {
            int lO = b.getLength();
            b.decrement(posB);
            if(b.subscriptAt(0) == "-")
            {
                b.setSubscript(1,d.decrement(BASE));
                b.deleteIndex(0);
            }
            if(lO > b.getLength())
            {
                posB--;
            }
        }
        
        while(posA >= 0)
        {
            v.add(0,new kNum(a.subscriptAt(posA)));
            posA--;
        }
        
        while(posB >= 0)
        {
            v.add(0,new kNum(b.subscriptAt(posB)));
            posB--;
        }
        
        while(v.size() > 1 && subscriptAt(0).equals("0"))
        {
            v.remove(0);
        }
    }
    
    /*/
    get a number with n zeroes (n kNum objects with subscript "0")
    
    @param n the number of zeroes
    @return BaseN the "number" with n zero-value kNums
    /*/
    BaseN nZeroes(int n)
    {
        ArrayList<kNum> rAL = new ArrayList<kNum>(n);
        for(int i = 0; i < n; i++)
        {
            rAL.add(new kNum("0"));
        }
        return new BaseN(BASE,rAL,false);
    }
    
    /*/
    get a BaseN number which is equal to the digit of the current number at index index
    
    @param index the index of the digit to get
    @return BaseN the number containing just the digit of current at index
    /*/
    BaseN digit(int index)
    {
        ArrayList<kNum> rAL = new ArrayList<kNum>(1);
        rAL.add(v.get(index));
        return new BaseN(BASE,rAL,false);
    }
    
    /*/
    *NOT AN ADDING ALGORITHM*
    Sequentially adds the digits of b to the current number
    
    @paran b
    @return BaseN (the digits of current)(the digits of b)
    /*/
    BaseN sequentialAdd(BaseN b)
    {
        ArrayList<kNum> rAL = new ArrayList<kNum>(v);
        rAL.addAll(b.getRepresentation());
        return new BaseN(BASE,rAL,isNegative);
    }
    
    /*/
    *DON'T USE THIS METHOD, EXCEPT FOR TESTING*
    really bad way of doing multiplication (don't use this method)
    I'm leaving it in as a way of testing that multiply(BaseN) still works
    
    @param bN the number to multiply by
    /*/
    void multiplyByConversion(BaseN bN)
    {
        String a = convertTo_10();
        String b = bN.convertTo_10();
        convert(d.multiply(a,b));
    }
    
    /*/
    take current * b (and set current to that value)
    
    @param b
    /*/
    void multiply(BaseN b)
    {
        if(BASE != b.getBase())return;
        BaseN a = new BaseN(BASE,new ArrayList<kNum>(v),isNegative);
        v.clear();
        v.add(new kNum("0"));
        
        int posA = a.getLength() - 1;
        String powA = "0";
        
        
        while(posA >= 0)
        {
            int posB = b.getLength() - 1;
            String powB = "0";
            String bSum = "0";
            while(posB >= 0)
            {
                bSum = d.add(bSum,d.multiply(d.multiply(b.subscriptAt(posB),d.pow(BASE,powB)),d.multiply(a.subscriptAt(posA),d.pow(BASE,powA))));
                powB = d.increment(powB);
                posB--;
            }
            BaseN temp = new BaseN(BASE);
            temp.convert(bSum);
            add(temp);
            posA--;
            powA = d.increment(powA);
        }
    }
}
