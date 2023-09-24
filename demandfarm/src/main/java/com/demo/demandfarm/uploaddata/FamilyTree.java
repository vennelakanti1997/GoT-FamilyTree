package com.demo.demandfarm.uploaddata;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

class Person {
    private String name;
    private int birthYear;
    private List<Person> children;

    public Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.children = new ArrayList<>();
    }

    public void addChild(Person child) {
        children.add(child);
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public List<Person> getChildren() {
        return children;
    }
}

public class FamilyTree {
    public static void main(String[] args) {
        // Create individuals
        Person grandparent1 = new Person("Grandparent 1", 1940);
        Person grandparent2 = new Person("Grandparent 2", 1950);
        Person parent1 = new Person("Parent 1", 1970);
        Person parent2 = new Person("Parent 2", 1980);
        Person child1 = new Person("Child 1", 2000);
        Person child2 = new Person("Child 2", 2010);

        // Establish relationships
        grandparent1.addChild(parent1);
        grandparent2.addChild(parent2);
        parent1.addChild(child1);
        parent1.addChild(child2);
        parent2.addChild(child2);

        // Print family tree
        printFamilyTree(grandparent1, 0);
    }

    public static void printFamilyTree(Person person, int generation) {


        System.out.println(new Gson().toJson(person));

        for (Person child : person.getChildren()) {
            printFamilyTree(child, generation + 1);
        }
    }
}
