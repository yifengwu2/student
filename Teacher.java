package com.stupra;

public class Teacher {
    //身份证
    private final int idCard;
    private final int teacherId;
    private final String sub;
    private final String name;
    private final String sex;
    private final int age;
    private final int teachTime;

    Teacher(Builder builder) {
        this.idCard = builder.idCard;
        this.teacherId = builder.teacherId;
        this.sub = builder.sub;
        this.name = builder.name;
        this.sex = builder.sex;
        this.age = builder.age;
        this.teachTime = builder.teachTime;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "idCard=" + idCard +
                ", teacherId=" + teacherId +
                ", sub='" + sub + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", teachTime=" + teachTime +
                '}';
    }

    static class Builder {
        private int idCard;
        private int teacherId;
        private String sub;
        private String name;
        private String sex;
        private int age;
        private int teachTime;

        public Builder setidCard(int idCard) {
            this.idCard = idCard;
            return this;
        }

        public Builder setTeacherId(int teacherId) {
            this.teacherId = teacherId;
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

        public Builder setTeachTime(int teachTime) {
            this.teachTime = teachTime;
            return this;
        }


        public Teacher build() {
            return new Teacher(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "idCard=" + idCard +
                    ", teacherId=" + teacherId +
                    ", sub='" + sub + '\'' +
                    ", name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", age=" + age +
                    ", teachTime=" + teachTime +
                    '}';
        }
    }

    public static void main(String[] args) {
        Teacher teacher = new Builder().setidCard(11).setAge(12).setName("小明").setSex("男").setTeacherId(2).setSub("物理").setTeachTime(123).build();
        System.out.println(teacher);
    }


}
