package ie.ncirl.student.x22249346.controller;

import java.util.ArrayList;
import java.util.List;

import ie.ncirl.student.x22249346.entity.Firm;
import ie.ncirl.student.x22249346.repository.FirmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FirmController {

  @Autowired
  private FirmRepository firmController;

  @GetMapping("/firms")
  public String getAll(Model model, @Param("keyword") String keyword) {
    try {
      List<Firm> firms = new ArrayList<Firm>();

      if (keyword == null) {
        firmController.findAll().forEach(firms::add);
      } else {
        firmController.findByTitleContainingIgnoreCase(keyword).forEach(firms::add);
        model.addAttribute("keyword", keyword);
      }

      model.addAttribute("firms", firms);
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "firms";
  }

  @GetMapping("/firms/new")
  public String addFirm(Model model) {
    Firm firm = new Firm();
    firm.setPublished(true);

    model.addAttribute("firm", firm);
    model.addAttribute("pageTitle", "Create new Firm");

    return "firm_form";
  }

  @PostMapping("/firms/save")
  public String saveFirm(Firm firm, RedirectAttributes redirectAttributes) {
    try {
      firmController.save(firm);

      redirectAttributes.addFlashAttribute("message", "The Firm has been saved successfully!");
    } catch (Exception e) {
      redirectAttributes.addAttribute("message", e.getMessage());
    }

    return "redirect:/firms";
  }

  @GetMapping("/firms/{id}")
  public String editFirm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Firm firm = firmController.findById(id).get();

      model.addAttribute("firm", firm);
      model.addAttribute("pageTitle", "Edit Firm (ID: " + id + ")");

      return "firm_form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());

      return "redirect:/firms";
    }
  }

  @GetMapping("/firms/delete/{id}")
  public String deleteFirm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      firmController.deleteById(id);

      redirectAttributes.addFlashAttribute("message", "The Firm with id=" + id + " has been deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/firms";
  }

  @GetMapping("/firms/{id}/published/{status}")
  public String updateFirmPublishedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean published,
      Model model, RedirectAttributes redirectAttributes) {
    try {
      firmController.updatePublishedStatus(id, published);

      String status = published ? "published" : "disabled";
      String message = "The Firm id=" + id + " has been " + status;

      redirectAttributes.addFlashAttribute("message", message);
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/firms";
  }
}
