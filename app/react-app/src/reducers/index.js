import { combineReducers } from 'redux'
import cart, * as fromCart from './cart'
import products, * as fromProducts from './products'
import customer, * as fromCustomer from './customer'
import { reducer as formReducer } from 'redux-form'


export default combineReducers({
  cart,
  products,
  customer,
  form: formReducer,
})

const getAddedIds = state => fromCart.getAddedIds(state.cart)
const getQuantity = (state, id) => fromCart.getQuantity(state.cart, id)
const getProduct = (state, id) => fromProducts.getProduct(state.products, id)

// TODO: rename selectors?
export const isActive = state => fromCustomer.isActive(state.customer)
export const getCustomerId = state => {
  return state.customer.customerId
}

export const getQuantityById = state => {
  return state.cart.quantityById
}

export const getTotal = state =>
  getAddedIds(state)
    .reduce((total, id) =>
      total + getProduct(state, id).price * getQuantity(state, id),
      0
    )
    .toFixed(2)

export const getTotalProducts = state =>
  getAddedIds(state)
    .reduce((total, id) =>
      total + getQuantity(state, id),
      0
    )

export const getCartProducts = state =>
  getAddedIds(state).map(id => ({
    ...getProduct(state, id),
    quantity: getQuantity(state, id)
  }))
