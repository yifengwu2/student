package com.student;

public class Student {
    //身份证
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

    public Student(int idCard, int age, String sex, String name, String sub, int number) {
        this.idCard = idCard;
        this.age = age;
        this.sex = sex;
        this.name = name;
        this.sub = sub;
        this.number = number;
    }

    public void setSocre(Score socre) {
        this.socre = socre;
    }

    @Override
    public String toString() {
        return "Student{" +
                "idCard=" + idCard +
                ", number=" + number +
                ", sub='" + sub + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", socre=" + socre +
                '}';
    }

     class Score {
        int EnglishScore;
        int JavaSocrere;
        int MathScore;

        public Score(int englishSocre, int javaSocrere, int mathSocre) {
            EnglishScore = englishSocre;
            JavaSocrere = javaSocrere;
            MathScore = mathSocre;
        }

        @Override
        public String toString() {
            return "{" +
                    "EnglishScore=" + EnglishScore +
                    ", JavaSocrere=" + JavaSocrere +
                    ", MathScore=" + MathScore +
                    '}';
        }
    }

    public static void main(String[] args) {
//       Score score = new Score(13, 42, 32);
        Student student = new Student(12345, 23, "男", "小明", "数据", 4312);
        Score score = student.new Score(11, 31, 42);
        student.setSocre(score);
        System.out.println(student);

    }
}
