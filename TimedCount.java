
/**
 * A testing method for the BaseN system. Tests efficiency of certain algorithms (addition, with the current implementation)
 * 
 * @author Daniel Taylor (Ambulator)
 * @version 13.11.17
 */
public class TimedCount
{
    public static void main(String[] args)
    {
        System.out.print("\f");
        Decimal d = new Decimal();
        String count = "0", base = "999999";
        String powNum = d.decrement(d.pow("10","10"));
        while(d.lessThan(base,"1000000"))
        {
            long sum = 0;
            BaseN addNum = new BaseN(base);
            addNum.convert(powNum);
            for(int i = 1; i <= 10; i++)
            {
                BaseN num = new BaseN(base);
                long duration = 1000 * 1000000;
                long c = 0;
                long start = System.nanoTime();
                
                while(System.nanoTime() < (start + duration))
                {
                    num.add(addNum);
                    c++;
                }
                
                sum += c;
            }
            System.out.println("base n_" + base + ":\t" + sum);
            count = d.increment(count);
            base = d.increment(base);
        }
    }
}
