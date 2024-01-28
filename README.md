# SampleInvoice
The system is an Energy Billing and Invoice Management System designed for a renewable energy company. It manages customer information, tariff plans, and generates monthly invoices based on energy consumption. The system supports two types of tariff plans: Fixed Rate and Tiered Rate.

## Entities:

### Customer:

Represents customers of the renewable energy company.
Contains information such as customer name, address, and associated tariff plan.
Each customer has a one-to-one relationship with a tariff plan.

### TariffPlan (Abstract Class):

An abstract class representing different types of tariff plans.
Two concrete implementations: FixedRateTariffPlan and TieredTariffPlan.
Includes common properties like name and description and introduces an abstract method getRatePerUnit() to retrieve the rate per unit of energy.

### FixedRateTariffPlan:

Extends TariffPlan.
Represents a tariff plan with a fixed rate per unit of energy.

### TieredTariffPlan:

Extends TariffPlan.
Represents a tariff plan with tiered rates, where different rates apply based on energy consumption tiers.

### Invoice:

Represents monthly invoices generated for customers.
Contains information such as customer name, energy consumed, issue date, due date, associated tariff plan, and billing strategy.
Utilizes a billing strategy to calculate the total amount based on energy consumption.

### BillingStrategy (Interface):

An interface defining the contract for different billing strategies.
Two implementations: FixedRateBillingStrategy and TieredBillingStrategy.
Contains a method calculateTotalAmount() to calculate the total amount based on energy consumption.

### FixedRateBillingStrategy:

Implements BillingStrategy.
Represents a billing strategy where the total amount is calculated using a fixed rate per unit.

### TieredBillingStrategy:

Implements BillingStrategy.
Represents a billing strategy where the total amount is calculated using tiered rates based on energy consumption.

### InvoiceItem:

Represents line items within an invoice, such as the description, quantity (energy consumed), and unit price.