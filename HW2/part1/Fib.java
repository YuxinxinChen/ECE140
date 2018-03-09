public class Fib extends Seq
{
        protected int first1;
        protected int first2;
        protected int last;

        public Fib(int first1, int first2, int last)
        {
                this.first1 =  first1;
                this.first2 =  first2;
                this.last = last;
        }

        public String toString()
        {
                return( "< "+String.valueOf(first1)+", "+String.valueOf(first2)+" to "+String.valueOf(last)+" >");
        }
}
