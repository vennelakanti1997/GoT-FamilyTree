import { useEffect, useState } from "react";
import { RequestData } from "../utils/Services";
import { GET_HOUSES } from "../utils/Constants";
import { Container, Dropdown, DropdownButton, Spinner } from "react-bootstrap";

const HousesDropDown = ({ houses, setHouseName }) => {
  const [title, setTitle] = useState("Select House");

  const handleOnSelect = (houseName) => {
    setTitle(houseName);
    setHouseName(houseName);
  };
  return (
    <>
      {houses instanceof Array && houses.length ? (
        <DropdownButton
          renderMenuOnMount={false}
          onSelect={(eventKey) => handleOnSelect(eventKey)}
          id="houses-button"
          title={title}
        >
          {houses.map((house) => (
            <Dropdown.Item key={house} eventKey={house}>
              {house}
            </Dropdown.Item>
          ))}
        </DropdownButton>
      ) : (
        <div className="text-danger">No Houses</div>
      )}
    </>
  );
};

export const SelectHouse = ({ setHouseName }) => {
  const [houses, setHouses] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    RequestData("GET", GET_HOUSES)
      .then((response) => setHouses(response.body))
      .catch((error) => window.alert(error))
      .finally(() => {
        setIsLoading(false);
      });
  }, []);

  return (
    <Container className="mt-5">
      {isLoading ? (
        <Spinner />
      ) : (
        <HousesDropDown houses={houses} setHouseName={setHouseName} />
      )}
    </Container>
  );
};
