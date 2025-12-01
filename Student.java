package com.student;

public class Student {
    //身份证
    private final int idCard;
    //学号
    private final int number;
    //专业
    private final String sub;
    //姓名
    private final String name;
    //性别
    private final String sex;

    private final int age;

    private  final Score score;

    private Student(Builder builder) {
        this.idCard = builder.idCard;
        this.sub = builder.sub;
        this.age = builder.age;
        this.number = builder.number;
        this.name = builder.name;
        this.sex = builder.sex;
        this.score =builder.socre;
    }


    static class Builder {
        private int idCard;
        //学号
        private int number;
        //专业
        private String sub;
        //姓名
        private String name;
        //性别
        private String sex;

        private int age;

        private Score socre;


        public Builder setIdCard(int idCard) {
            this.idCard = idCard;
            return this;
        }

        public Builder setNumber(int number) {
            this.number = number;
            return this;

        }

        public Builder setSub(String sub) {
            this.sub = sub;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSex(String sex) {
            this.sex = sex;
            return this;

        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setSocre(Score socre) {
            this.socre = socre;
            return this;
        }

        public Student build() {
            return new Student(this);
        }


    }

//    private void setSocre(Score socre) {
//        this.socre = socre;
//    }

    @Override
    public String toString() {
        return "Student{" +
                "idCard=" + idCard +
                ", number=" + number +
                ", sub='" + sub + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", score=" + score +
                '}';
    }

    static class Score {
        int EnglishScore;
        int JavaScore;
        int MathScore;

        public Score(int EnglishScore, int JavaScore, int MathScore) {
            this.EnglishScore = EnglishScore;
            this.JavaScore = JavaScore;
            this.MathScore = MathScore;
        }

        @Override
        public String toString() {
            return "{" +
                    "EnglishScore=" + EnglishScore +
                    ", JavaScore=" + JavaScore +
                    ", MathScore=" + MathScore +
                    '}';
        }
    }

    public static void main(String[] args) {
        Score score = new Score(13, 42, 32);
        Student student = new Builder()
                .setSocre(score)
                .setAge(11)
                .setName("xx")
                .setIdCard(123)
                .setSub("fas")
                .setSex("男")
                .setNumber(1324)
                .build();
        System.out.println(student);

    }
}
