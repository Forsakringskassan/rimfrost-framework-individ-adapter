package se.fk.rimfrost.adapter.individ.model;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface Individ
{
   UUID id();

   String typ();

   String varde();
}
