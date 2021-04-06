package BuissnessLayer.SupplierBuissness;

import BuissnessLayer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SuppliersControler implements ISupplierControler {
    private static SuppliersControler single_instance = null;
    private HashMap<Integer, ISupplier> Suppliers;
    private AgreementManager agreementManager;
    private int SupplierIdCounter;

    public SuppliersControler(AgreementManager agreementManager) {
        Suppliers = new HashMap<>();
        this.agreementManager=agreementManager;
        SupplierIdCounter=0;
    }
    //private function
    private void AddNewAgreement(IAgreement agreement) {
        agreementManager.AddNewAgreement(agreement);

    }

    public void removeProductFromSupplier(int CatalogId,int SupID){
        agreementManager.GetAgreement(SupID).RemovePrudact(CatalogId);
    }

    public void setProductPrice(int SupId, int CatalogID, double price){
        agreementManager.GetAgreement(SupId).setProductPrice(CatalogID,price);
    }

    public void addNewDiscountByQuantitiyToProduct(int SupId, int CatalogID, int Quantitiy, double newPrice) {
    agreementManager.GetAgreement(SupId).AddDiscountByProduct(CatalogID,Quantitiy,newPrice);
    }

    public void RemovDiscountByQuantitiyToProduct(int SupId, int CatalogID, int Quantitiy) {
        agreementManager.GetAgreement(SupId).removeDiscountQuantity(CatalogID,Quantitiy);
    }

    public void setExtraDiscountToSupplier(int SupId, int ExtraDiscount) {
        (agreementManager.GetAgreement(SupId)).SetExtraDiscount(ExtraDiscount);
    }
        public IAgreement getAgreement(int SupId) {
        if (!isSupplierExist(SupId)) {
            throw new IllegalArgumentException("the supplier does not exist");
        }
        return (agreementManager.GetAgreement(SupId));
    }

    @Override
    public void SetPayment(paymentMethods paymentMethods, int SupplierId) {
        if (!isSupplierExist(SupplierId)) {
            throw new IllegalArgumentException("the supplier does not exist");
        }
        ISupplier supplier = Suppliers.get(SupplierId);
        supplier.setPayment(paymentMethods);
    }

    //@Override
    public void addNewProductToAgreement(int SupplierId, double Price, int CatalogID, String manfucator, String name) {
        if(!isSupplierExist(SupplierId)){
            throw new IllegalArgumentException("the Supplier doues not exsist");
        }
        if(manfucator==null||manfucator==""){
            throw new IllegalArgumentException("the manfucator input is incorrect");
        }
        if(name==null||name==""){
            throw new IllegalArgumentException("the name input is incorrect");
        }
        agreementManager.AddProduct(SupplierId,Price,CatalogID,manfucator,name);
    }

    @Override
    public void addNewSupplier(int id, String name, String bankAccount, paymentMethods paymentMethods, DeliveryMode deliveryMode, List<Integer> daysOfDelivery, int NumOfDaysFromDelivery) {
        if (isSupplierExist(id)) {
            throw new IllegalArgumentException("the supplier does exist already");
        }
        Supplier supplier = new Supplier(id, name, paymentMethods, bankAccount, SupplierIdCounter);
        Suppliers.put(id, supplier);
        SupplierIdCounter++;
        Agreement agreement=new Agreement(id,deliveryMode, daysOfDelivery,NumOfDaysFromDelivery);
        AddNewAgreement(agreement);
    }

    @Override
    public void RemoveSupplier(int SupId) {

        if (!isSupplierExist(SupId)) {
            throw new IllegalArgumentException("the supplier does not exist");
        }
        Suppliers.remove(SupId);
        agreementManager.RemoveAgreement(SupId);

    }


    @Override
    public ISupplier getSupplier(int SupId) {
        if (!isSupplierExist(SupId)) {
            throw new IllegalArgumentException("the supplier does not exist");
        }
    return Suppliers.get(SupId);
    }

    @Override
    public List<ISupplier> getAllSuppliers() {
        return new ArrayList<ISupplier>(Suppliers.values());
    }

    @Override
    public void setBankAccount(int SupId, String BankAccount) {
        if(BankAccount==null){
            throw new IllegalArgumentException("the Bank Acount is null");
        }
        if(BankAccount==""){
            throw new IllegalArgumentException("the Bank Acount value is empty");
        }
        if (!isSupplierExist(SupId)) {
            throw new IllegalArgumentException("the supplier does not exist");
        }
        Suppliers.get(SupId).setBankAccount(BankAccount);
    }

    @Override
    public void addNewContact(int SupId, String contactName, String contactEmail, String phoneNumber) {

        if(contactEmail==null||contactName==null||phoneNumber==null){
            throw new IllegalArgumentException("one of the given values is null");
        }
        if(contactEmail==""||contactName==""||phoneNumber==""){
            throw new IllegalArgumentException("one of the given values is empty");
        }
        if(!validatePhoneNumber(phoneNumber)){
            throw new IllegalArgumentException("the phone number form is incorect");
        }
        if (!isSupplierExist(SupId)) {
            throw new IllegalArgumentException("the supplier does not exist");
        }
        if(!isValidEmail(contactEmail)){
            throw new IllegalArgumentException("the Email is not valid");

        }
        Suppliers.get(SupId).addNewContact(contactName,phoneNumber,contactEmail);
    }

    @Override
    public void removeContact(int SupId, int ContactId) {
        if (!isSupplierExist(SupId)) {
            throw new IllegalArgumentException("the supplier does not exist");
        }
        Suppliers.get(SupId).removeContact(ContactId);
    }

    // return true if the supplier exist in the suppliers hash map
    public boolean isSupplierExist(int supplierId){
        return Suppliers.containsKey(supplierId);
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    private  boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{10}"))
            return true;
        else if(phoneNumber.matches("\\d{9}")){
            return true;
        }
        return false;
    }
}
