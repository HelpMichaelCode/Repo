package models;

public class Restock extends Product {
    private int restock;

    public Restock(){

    }

    public Restock(Long id, String name, String desc, double price, int qty, int restock) {
        super(id, name, desc, price, qty);
        this.restock = restock;
    }

    public Restock(Product p){
        super(p.getProductID(), p.getProductName(), p.getProductDescription(), p.getProductPrice(), p.getProductQty());
    }

    public int getRestock(){
        return restock;
    }

    public void setRestock(int restock){ //to compare
        this.restock = restock;
    }
}