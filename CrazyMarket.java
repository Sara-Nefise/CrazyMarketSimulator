import java.util.Iterator;
import java.util.Random;
import javax.swing.text.Position;
import java.util.Scanner;


public class CrazyMarket implements MyQueue<Customer>{
	double bekleme_time=0; //golbal bekleme zamani musterilerin bekleme zamani saklar
    double total_time=0.0;// musterilerin eklenecegi ana kadar toplam gelis zamanlari
    int BefCustomersertime; //bir onceki musterilerin hizmet zamani
    private class Node{
        private Node next;
		Customer data;
        public Node(Customer data) {
			this.data = data;
			this.next = null;
        }
	}
	Node head=null;
    Node tail=null;


    public Customer(){ //constractor
		arrivalTime=0;
		removalTime=0;
    };
    
    Random rand = new Random();
	/**
	 *  numberOfCustumer ile verilen sayida  
	 * musteri hizmet gorene kadar calismaya devam eder*/
	public CrazyMarket(int numberOfCustomer) {

		while(numberOfCustomer>0){ //bu fonksiyon 
            Customer new_one=new Customer(); //create customer
            if(isEmpty()){
                double coming_time =rand.nextInt(3); //[0-2]
                double service_time =rand.nextInt(3)+1; //[1-3]
                new_one.removalTime=service_time;//customerinin himzet degeri 
                BefCustomersertime=service_time; //ilk musteri hizmet zamani
                new_one.arrivalTime=coming_time; //customerin gelis zamani
                total_time+=coming_time; //toplam gelis zamani 1.musteri 0.2 + 2.musteri 1.9 gibi
                enqueue(new_one);
            }else if(10<bekleme_time && isEmpty()==false){
                double coming_time =rand.nextInt(3); //[0-2]
                double service_time =rand.nextInt(3)+1; //[1-3]
                new_one.arrivalTime=coming_time+total_time;
                new_one.removalTime=BefCustomersertime-new_one.arrivalTime;
                BefCustomersertime+=service_time; //işelm bittikten sonra ekler.
                bekleme_time+=new_one.removalTime; //golbal bekleme zamanina ekler.
                total_time+=new_one.arrivalTime; //1.musteri 0.2 de gelsin 2.musteri 2 saniye sonra yani 0.2 + 2 = 2.2,
                // 0.2 degeri yani bi onceki degeri tutmak icin 
                enqueue(new_one);
            }else if(10>bekleme_time && isEmpty()==false){
                dequeuNext();
                numberOfCustomer--;
            }
        
		}	
		
	}

	/**
	 *  numberOfCustumer ile verilen sayida  
	 * musteri hizmet gorene kadar calismaya devam eder, 
	 * calisirken verilen tekerlemeyi kullanir*/
	public CrazyMarket(int numberOfCustomer, String tekerleme) {
        while(numberOfCustomer>0){ //bu fonksiyon 
            Customer new_one=new Customer(); //create customer
            if(isEmpty()){
                double coming_time =rand.nextInt(3); //[0-2]
                double service_time =rand.nextInt(3)+1; //[1-3]
                new_one.removalTime=service_time;//customerinin himzet degeri 
                BefCustomersertime=service_time; //ilk musteri hizmet zamani
                new_one.arrivalTime=coming_time; //customerin gelis zamani
                total_time+=coming_time; //toplam gelis zamani 1.musteri 0.2 + 2.musteri 1.9 gibi
                enqueue(new_one);
            }else if(10<bekleme_time && isEmpty()==false){
                double coming_time =rand.nextInt(3); //[0-2]
                double service_time =rand.nextInt(3)+1; //[1-3]
                new_one.arrivalTime=coming_time+total_time;
                new_one.removalTime=BefCustomersertime-new_one.arrivalTime;
                BefCustomersertime+=service_time; //işelm bittikten sonra ekler.
                bekleme_time+=new_one.removalTime; //golbal bekleme zamanina ekler.
                total_time+=new_one.arrivalTime; //1.musteri 0.2 de gelsin 2.musteri 2 saniye sonra yani 0.2 + 2 = 2.2,
                // 0.2 degeri yani bi onceki degeri tutmak icin 
                enqueue(new_one);
            }else if(10>bekleme_time && isEmpty()==false){
                dequeuWithCounting(tekerleme);
                numberOfCustomer--;
            }
        
		}

		
	}
	/** kuyrugun basindaki musteriyi yada tekerleme 
	 * ile buldugu musteriyi return eder*/
	public Customer chooseCustomer() {
		if(bekleme_time>10){
			return dequeuNext();
		}else{
			return dequeuWithCounting(tekerleme);
		}
	}

	@Override
	public int size() {
		int counter=0;
		Node temp=head; //head nodu tutar
		if(temp!=null){ //queuenin hepsine gezene kadar
			temp=temp.next;
			counter++;
		}
		return counter;
	}

	@Override
	public boolean isEmpty() {
		return (size()==0)? true: false;
	}

	@Override
	public boolean enqueue(Customer item) {
		if(isEmpty()){ //ilk node ise
			Node new_one=new Node(item);
			head=new_one;
			tail=new_one;
			return true;
		}else{	
			Node new_one=new Node(item);
			head.next=new_one;
			tail=new_one;
			return true;
		}
		return false;
	}

	@Override
	public Customer dequeuNext() {
		if(isEmpty()){ //bos ise 
			System.out.println("it is empty you can't remove element now");
			return -1;
        }
        bekleme_time-=head.data.removalTime; //bekleme zamanindan cikaririr
        total_time-=head.data.arrivalTime; //gelis zamanindan cikart 0.2 +2 olacakti ya-
        // head 0.2 olsun o cikinca 0.2 degeri silinsin
		System.out.println("BEKLEME SÜRESİ: "+head.data.removalTime);
		Node cikanmusteri=head; //cikan musteri return etmek icin
		Node temp=head.next;
		head.data=null;
		head=temp;
		return cikanmusteri;
	}

	@Override
	public Customer dequeuWithCounting(String tekerleme) {
		double service_time =rand.nextInt(3)+1; //[1-3]
		Node temp=head;
		Node cikanmusteri; //cikan musteri return etmek icin
		int counter=0; //hece sayisi tutan degisken
		int j=0; //nodlari gezmek icin
		//hece sayisi bulmak icin 
		for(int i=0; i<tekerleme.length(); i++){
			char harf=tekerleme.toLowerCase().charAt(i);
			if(harf=='u'||harf=='i'||harf=='o'||harf=='e'||harf=='a'){
				counter++;
			}
		}
		while(temp!=null && j<counter-2){ //silenecek elemeni bulmak icin while dongusu
			if(temp.next==null){ //eger Queuenin sonuna gelirse Queuenin sonu heade bagla.
				temp.next=head; 
				j++;
			}
			temp=temp.next;
			j++;
		}
		if(temp.next==null){ //buldugumuz nodun nexti Queuenin sonuna gelirse Queuenin sonu heade bagla.
			temp.next=head;
		}
		else if(temp.next.next==null){ //buldugumuz nodun next.nexti eger Queuenin sonuna gelirse Queuenin sonu heade bagla.
			temp.next.next=head;
		}
		cikanmusteri=temp.next;
        bekleme_time-=cikanmusteri.data.removalTime; //bekleme zamanindan cikaririr
        total_time-=cikanmusteri.data.arrivalTime; //gelis zamanindan cikart 0.2 +2 olacakti ya-
        // head 0.2 olsun o cikinca 0.2 degeri silinsin
		System.out.println("BEKLEME SÜRESİ: "+ cikanmusteri.data.removalTime);
		Node baglama=temp.next.next; //bir sonraki noda baglamak icin 
		temp.next=baglama;		

		return cikanmusteri;
	}
	@Override
	public Iterator<> iteratCustomeror() {
		return StackIterator();
	}
	private class StackIterator implements Iterator<Customer>{
        private Node itr = head;
        @Override
        public boolean hasNext() {
            return itr != null;
        }

        @Override
        public Customer next() {
            Customer data = itr.data;
            itr = itr.next; 
            return data;
		}
    }
    
    String tekerleme = "O piti piti karamela sepeti "
			+ "\nTerazi lastik jimnastik "
			+ "\nBiz size geldik bitlendik Hamama gittik temizlendik.";
}
