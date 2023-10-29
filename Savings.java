public class Savings {
    public static void main(String args[]){
        double initBalance=100000.0;
        double monthDeposit=1000.0;
        double interestRate=0.0;
        double balance=initBalance;
        System.out.println("Month\tinterest rate\tInterest\t Balance");
        for(int month=1;month<=12;month++){
            if(balance<=110000){
                interestRate=0.01;
            }
            else if(balance<=125000){
                interestRate=0.015;
            }
            else{
                interestRate=0.02;
            }
            double interest=balance*interestRate;
            balance+=interest+monthDeposit;
            System.out.printf("%d\t%.2f\t\t%.2f\t\t%.2f\n",month,interestRate,interest,balance);
        }
    }
}
