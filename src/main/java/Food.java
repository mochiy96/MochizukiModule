public class Food {
    public String name, description;
    public FoodColors foodColors;
    public Nutrients nutrients;
    public int cost;

    @Override
    public String toString(){
        return "name = " + name;
    }

    public String getName(){
        return name;
    }
    public String getDexcription(){
        return description;
    }
}

class FoodColors{
    public double red, green, yellow;
}

class Nutrients{
    public int energy, vegetable;
    public double protein, lipid, carbohydrate, salt, calcium;
}