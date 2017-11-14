
/**
 * 
 * 
 * @author Daniel Taylor (DTIII)
 * @version .._-
 */
public class TimedCount
{
    public static void main(String[] args)
    {
        System.out.print("\f");
        //         int iterations = 128000;
        //         int sleeptime = 1;
        Decimal d = new Decimal();
        String count = "0", base = "999999";
        String powNum = d.decrement(d.pow("10","10"));
        while(d.lessThan(base,"1000000"))
        {
            //String sum = "0";
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
                    //num.increment();
                }
                
                //num.print();
                //sum = d.add(sum,num.convertTo_10());
                sum += c;
            }
            System.out.println("base n_" + base + ":\t" + sum);
            count = d.increment(count);
            base = d.increment(base);
            // this process proves that n base 9 is 52% more efficient than just base 10, and is, in fact the most efficient base
            //however, even n_9 is 82% less efficient on average than just incrementing a long
        }
    }
}