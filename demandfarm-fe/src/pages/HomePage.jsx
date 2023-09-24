import { SelectHouse } from "../components/SelectHouse";
import { RequestData } from "../utils/Services";
import { GET_HOUSE_MEMBERS } from "../utils/Constants";
import { useEffect, useState } from "react";
import { HouseTree } from "../components/HouseTree";

export const HomePage = () => {
  const [houseName, setHouseName] = useState("");
  const [houseMembers, setHouseMembers] = useState({});

  useEffect(() => {
    if (houseName) {
      RequestData("GET", `${GET_HOUSE_MEMBERS}/${houseName}`)
        .then((response) => {
          setHouseMembers(response.body);
        })
        .catch((error) => window.alert(error));
    }
  }, [houseName]);
  return (
    <>
      <SelectHouse setHouseName={setHouseName} />
      <HouseTree
        houseMembers={houseMembers}
        houseName={houseName}
        setHouseMembers={setHouseMembers}
      />
    </>
  );
};
