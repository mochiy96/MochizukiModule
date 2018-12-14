public class Food {
    public String name, description;
    public FoodColors foodColors;
    public Nutrients nutrients;
    public int cost;

    @Override
    public String toString(){
        return "name = " + name;
    }
}

class FoodColors{
    public double red, green, yellow;
}

class Nutrients{
    public int energy, vegetables;
    public double protein, lipid, carbohydrate, salt, calcium;
}