# White box and mock tests

## Reproducing coverage results

**Note:** You will need to run the tests twice, unless tests that should pass will fail, I believe this may be related to using an unstable version of mockito, I talked to the professor about this problem but we couldn't find a solution

## White box Coverage
![Screenshot 2024-04-24 at 2.39.46 PM.png](..%2F..%2F..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fsx%2F352rtltn5kx8dytr_5zgzfg00000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_KHdRqK%2FScreenshot%202024-04-24%20at%202.39.46%E2%80%AFPM.png)

## Limitations to White box testing

**Unfortunately we could not reach 100% code coverage for white box testing due to limitations we were unaware of at the time of project proposal, limitations we highlight below**

### BucketItem
- Equals method
  - name and filling are private variables and we can't change these variables to increase branch coverage
- interactOn method
  - Similarly filling is a private variable and can't control filling for different fills

### ClothingItem
- interactOn method
  - getAllInstances method always returns instances of clothingItems, so we cannot test a case where this is false

### Inventory
- add method
  - unlimited is a protected variable, there is no where in the class where unlimited is changed to true, so there's no way to test cases where unlimited is true
- removeFromStack method
  - removed at max can only be equals to amountRemaining due to operations in function, hence the condition removed > count will never be reached
- removeItems method
  - count > 0 relies on removeFromStack having removed > count, since this is never possible this condition can never be true

### PotionItem
- apply position method
  - Due to the logic of the overloaded apply position helper method, it's not possible for result to be true and time to be <= 0
- Equals method
  - Since we don't have access to the name field, we have to work with the instances of potionItems available to us, however there is no case where the names are different but the types are the same or vice versa

### PotionType
- toggle effect method for escape
  - Depth is a final variable for level class. Since level doesn't have a public method and there's no way to mock a different final variable, we couldn't test the states of playerdepth != 0

### Recipes
- main function
  - The logic in main is used for GUI testing, since there's no output to test or input to manipulate and we are not testing GUI we were unable to test the main method
  - **Note:** even without explicitly testing the main function, we still test over 2000 loc

### SummonItem
- interact on method
  - Depth is a final variable for level class. Since level doesn't have a public method and there's no way to mock a different final variable, we couldn't test the states of playerdepth != 0

### TileItem
- get all instances method
  - The logic to mock the conditions that will allow for full branch coverage of this function are well outside the items method.
- TileItem constructor
  - There is no way to call this method as it's protected
- interact on method, equals method and hashCode method
  - TileItem has a protected constructor, there is no way to test the case in which the model is null

### ToolItem
- isDepleted method
  - type.durability is final and always greater than 0

**Note**: We used extensive mock testing and mocking in our white box testing, that is we used mock testing hand in hand in our white box tests.

## Discovered faults
1. **testRemoveItemNoneStackingItemManyRemove** reveals a fault in the **inventor** class, inventory loops until i == size of the list of items being stored, however if you remove an item you change the size of the list, so let's say the inventory has two items and you remove the first item, the loop will terminate before checking whether to remove the second item. A fix would be to store before hand the initial size of the list and use this size for loop termination.
2. **testConstructInvalidReqItems** and **testConstructorInvalidCreatedItems** reveals a fault in the constructor of the **Recipe** class, namely there is no error checking of the input for the constructor and no throws annotations are written on the class, although it you pass in a poorly formatted string, an error will occur
3. **testGetAttackDamageBonusDurMobPickaxe** reveals a fault in the getAttackDamageBonus method of the **ToolItem** class, the attack damage bonus is stated to always be between 3 and 6 for wooden pickaxes, however this is not the case, the likely fault is that there is a math error in the calculation of a random bonus damage between these values

