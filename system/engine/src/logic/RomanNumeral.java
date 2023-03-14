package logic;

public enum RomanNumeral {

    I{
    public int evaluateNumber(){return 1;}
    },
    II{
        public int evaluateNumber(){return 2;}
    },
    III{
        public int evaluateNumber(){return 3;}
    },
    IV{
        public int evaluateNumber(){return 4;}
    },
    V{
        public int evaluateNumber(){return 5;}
    };

    abstract int evaluateNumber();
}
